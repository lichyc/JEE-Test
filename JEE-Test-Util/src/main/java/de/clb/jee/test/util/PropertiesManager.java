/**
 * 
 */
package de.clb.jee.test.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.TreeSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.jboss.vfs.VirtualFile;

/**
 * @author clichybi
 * 
 */
public class PropertiesManager {
	
	static Logger log = Logger.getLogger("PropertiesManager") ;
	
	private  HashMap<String, String> inMemoryProperties;
	
	private String packageRoot = "/";
	
	private static PropertiesManager myself;
	
	private PropertiesManager(String packageRoot) {
		if (null != packageRoot) {
			this.packageRoot = packageRoot;
		}
		loadPropertiesFiles();
		
	}
	
	public static PropertiesManager getInstance(String packageRoot) {
		if(null == myself) {
			myself = new PropertiesManager(packageRoot);
		}
		
		return myself;
	}
	
	public static PropertiesManager getInstance() {
		return getInstance(null);
	}
	
	
	public void loadPropertiesFiles() {
		
		String[] urls = getResourcesInPackage(packageRoot, ".*properties");
		
		HashMap<String, String> tmpMap = new HashMap<String, String>();

		ClassLoader classLoader = PropertiesManager.class.getClassLoader();
		assert classLoader != null;

		for (int i = 0; i < urls.length; i++) {
			InputStream is = classLoader.getResourceAsStream(urls[i]);
			if (null != is) {
				Properties props = new Properties();
				try {
					props.load(is);
					
					Enumeration<Object> myEnum = props.keys();
					while (myEnum.hasMoreElements()) {
						String key = (String) myEnum.nextElement();
						if(tmpMap.containsKey(key)) {
							log.warning("Property "+key+" already exists - value "+ tmpMap.get(key)+" will be overwritten, by "+props.getProperty(key)+"! - Check properties files on consistence.");
						}
						tmpMap.put(key, props.getProperty(key));
					}
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
		}
		
		inMemoryProperties = tmpMap;
		
	}
	
	public String getProperty(String key) {
		return inMemoryProperties.get(key);
	}
	
	
	/**
	 * Recursive method used to find all classes in a given path (directory or
	 * zip file url). Directories are searched recursively. 
	 * 
	 * @param url
	 *            The base directory or url from which to search.
	 * @param packageName
	 *            The package name for classes found inside the base directory
	 * @param regex
	 *            an optional class name pattern. e.g. .*Test
	 * @return The classes
	 */
	private static TreeSet<String> findResources(URL url, String packageName,
			Pattern regex) throws Exception {
		ClassLoader classLoader = PropertiesManager.class.getClassLoader();
		assert classLoader != null;

		TreeSet<String> urls = new TreeSet<String>();

		if (url.getProtocol().equals("jar")) {
			String jarFileName;
			JarFile jf;
			Enumeration<JarEntry> jarEntries;
			String entryName;

			// build jar file name, then loop through zipped entries
			jarFileName = URLDecoder.decode(url.getFile(), "UTF-8");
			jarFileName = jarFileName.substring(5, jarFileName.indexOf("!"));
			log.info("processing external jar: " + jarFileName);
			jf = new JarFile(jarFileName);
			jarEntries = jf.entries();
			while (jarEntries.hasMoreElements()) {
				entryName = jarEntries.nextElement().getName();
				if (entryName.startsWith(packageName)
						&& (regex == null || regex.matcher(entryName).matches())) {
					urls.add(entryName);
					log.info("Add resource: " + entryName);
				}
			}
		} else {

			File dir = null;
			if (url.getProtocol().equalsIgnoreCase("vfs")) {
				log.info("processing resource in jboss vfs: " + url);
				URLConnection conn = url.openConnection();
				VirtualFile vf = (VirtualFile) conn.getContent();
				if (vf.isDirectory()) {
					List<VirtualFile> childList = vf.getChildrenRecursively();
					for (VirtualFile childVf : childList) {
						if (childVf.isFile()) {
							if (regex == null
									|| regex.matcher(childVf.getName())
											.matches()) {
								urls.add(childVf.asFileURL().getFile());
								log.info("Add resource: "
										+ childVf.asFileURL() + " : "
										+ childVf.getName());
							}
						}
					}
				}

			} else {
				dir = new File(url.getPath());

				if (!dir.exists()) {
					log.warning("path not exists: " + url);
					return urls;
				}

				File[] files = dir.listFiles();
//				log.info("processing dir: " + url);
				for (File file : files) {
					log.finest("processing file system resource: " + file.getName());
					if (file.isDirectory()) {
						assert !file.getName().contains(".");
						urls.addAll(findResources(file.toURI().toURL(),
								packageName + "." + file.getName(), regex));
					} else {
						String resourceName = file.getAbsolutePath();
						if (regex == null
								|| regex.matcher(resourceName).matches()) {
							urls.add(resourceName);
							log.info("Add resource: " + resourceName
									+ " : " + file.getName());
						}
					}
				}
			}
		}
		return urls;
	}

	

	public static String[] getResourcesInPackage(String packageName,
			String regexFilter) {
		Pattern regex = null;
		if (regexFilter != null)
			regex = Pattern.compile(regexFilter);

		try {
			ClassLoader classLoader = PropertiesManager.class.getClassLoader();
			assert classLoader != null;
			String path = packageName.replace('.', '/');
			Enumeration<URL> resources = classLoader.getResources(path);
			List<URL> dirs = new ArrayList<URL>();
			while (resources.hasMoreElements()) {
				URL resource = resources.nextElement();
				dirs.add(resource);
				log.info("Located package in: " + resource.getFile());
			}

			TreeSet<String> urls = new TreeSet<String>();
			for (URL directory : dirs) {
				urls.addAll(findResources(directory, packageName, regex));
			}

			ArrayList<String> urlList = new ArrayList<String>();

			for (String url : urls) {
				for (URL dir : dirs) {
					if (url.startsWith(dir.getPath())) {
						String finalResourceName = null;
						if(packageName.endsWith("/")) {
							finalResourceName = url.replace(dir.getPath(), packageName);
						} else {
							finalResourceName = url.replace(dir.getPath(), packageName.replace(".", "/")+"/");
						}
						log.info("Final Resource Name: " + finalResourceName);
						urlList.add(finalResourceName);
					}
				}
			}

			return urlList.toArray(new String[urls.size()]);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void printPropertiesContent(String[] urls) {

		ClassLoader classLoader = PropertiesManager.class.getClassLoader();
		assert classLoader != null;

		for (int i = 0; i < urls.length; i++) {
			InputStream is = classLoader.getResourceAsStream(urls[i]);
			if (null != is) {
//				log.info("SUCCESS !!!!  System-Classloader: "
//						+ classLoader.toString());
//				log.info("SUCCESS !!!!  PropertiesLoader: "
//						+ is.toString());
				Properties props = new Properties();
				try {
					props.load(is);
					
					Enumeration myEnum = props.keys();
					while (myEnum.hasMoreElements()) {
						Object key = (Object) myEnum.nextElement();
						log.info("ENTRY: key: " + key + " value: "
								+ props.get(key));
					}
				} catch (IOException e) {

					e.printStackTrace();
				}
			} else {
				is = classLoader.getSystemResourceAsStream(urls[i]);
				if (null != is) {
					log.info("PropertiesLoader: " + is.toString());
				} else {
					log.log(java.util.logging.Level.SEVERE, "FAILURE !!!!  Classloader: "
							+ classLoader.toString());

					log.log(java.util.logging.Level.SEVERE, "FAILURE !!!!  System-Classloader: "
							+ classLoader.getSystemClassLoader().toString());

				}
			}
		}

	}
	
	public void printInMemoryProperties() {
		Iterator<String> myEnum = inMemoryProperties.keySet().iterator();
		
		while (myEnum.hasNext()) {
			String key = (String) myEnum.next();
			log.info("inMemoryProperty: key: " + key + " value: "
					+ inMemoryProperties.get(key));
		}
	}

	public static void main(String[] args) {

		String[] urls = PropertiesManager.getResourcesInPackage("de.clb.jee.test",
				".*properties");

		PropertiesManager.printPropertiesContent(urls);

	}

}
