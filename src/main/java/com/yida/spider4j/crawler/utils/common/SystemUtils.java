package com.yida.spider4j.crawler.utils.common;
/**
 * Java System工具类
 * @author  Lanxiaowei
 * @created 2013-09-05 14:11:10
 */
public class SystemUtils {
private static final String OS_NAME_WINDOWS_PREFIX = "Windows";
    
    private static final String USER_HOME_KEY = "user.home";

    private static final String USER_DIR_KEY = "user.dir";
    
    private static final String JAVA_IO_TMPDIR_KEY = "java.io.tmpdir";
    
    private static final String JAVA_HOME_KEY = "java.home";
    
    public static final String AWT_TOOLKIT = getSystemProperty("awt.toolkit");

    public static final String FILE_ENCODING = getSystemProperty("file.encoding");

    public static final String FILE_SEPARATOR = getSystemProperty("file.separator");

    public static final String JAVA_AWT_FONTS = getSystemProperty("java.awt.fonts");

    public static final String JAVA_AWT_GRAPHICSENV = getSystemProperty("java.awt.graphicsenv");

    public static final String JAVA_AWT_HEADLESS = getSystemProperty("java.awt.headless");

    public static final String JAVA_AWT_PRINTERJOB = getSystemProperty("java.awt.printerjob");

    public static final String JAVA_CLASS_PATH = getSystemProperty("java.class.path");

    public static final String JAVA_CLASS_VERSION = getSystemProperty("java.class.version");

    public static final String JAVA_COMPILER = getSystemProperty("java.compiler");

    public static final String JAVA_ENDORSED_DIRS = getSystemProperty("java.endorsed.dirs");

    public static final String JAVA_EXT_DIRS = getSystemProperty("java.ext.dirs");

    public static final String JAVA_HOME = getSystemProperty(JAVA_HOME_KEY);

    public static final String JAVA_IO_TMPDIR = getSystemProperty(JAVA_IO_TMPDIR_KEY);

    public static final String JAVA_LIBRARY_PATH = getSystemProperty("java.library.path");

    public static final String JAVA_RUNTIME_NAME = getSystemProperty("java.runtime.name");

    public static final String JAVA_RUNTIME_VERSION = getSystemProperty("java.runtime.version");

    public static final String JAVA_SPECIFICATION_NAME = getSystemProperty("java.specification.name");

    public static final String JAVA_SPECIFICATION_VENDOR = getSystemProperty("java.specification.vendor");

    public static final String JAVA_SPECIFICATION_VERSION = getSystemProperty("java.specification.version");

    public static final String JAVA_UTIL_PREFS_PREFERENCES_FACTORY = getSystemProperty("java.util.prefs.PreferencesFactory");

    public static final String JAVA_VENDOR = getSystemProperty("java.vendor");

    public static final String JAVA_VENDOR_URL = getSystemProperty("java.vendor.url");

    public static final String JAVA_VERSION = getSystemProperty("java.version");

    public static final String JAVA_VM_INFO = getSystemProperty("java.vm.info");

    public static final String JAVA_VM_NAME = getSystemProperty("java.vm.name");

    public static final String JAVA_VM_SPECIFICATION_NAME = getSystemProperty("java.vm.specification.name");

    public static final String JAVA_VM_SPECIFICATION_VENDOR = getSystemProperty("java.vm.specification.vendor");

    public static final String JAVA_VM_SPECIFICATION_VERSION = getSystemProperty("java.vm.specification.version");

    public static final String JAVA_VM_VENDOR = getSystemProperty("java.vm.vendor");

    public static final String JAVA_VM_VERSION = getSystemProperty("java.vm.version");

    public static final String LINE_SEPARATOR = getSystemProperty("line.separator");

    public static final String OS_ARCH = getSystemProperty("os.arch");

    public static final String OS_NAME = getSystemProperty("os.name");

    public static final String OS_VERSION = getSystemProperty("os.version");

    public static final String PATH_SEPARATOR = getSystemProperty("path.separator");

    public static final String USER_COUNTRY = getSystemProperty("user.country") == null ?
        getSystemProperty("user.region") : getSystemProperty("user.country");

    public static final String USER_DIR = getSystemProperty(USER_DIR_KEY);

    public static final String USER_HOME = getSystemProperty(USER_HOME_KEY);

    public static final String USER_LANGUAGE = getSystemProperty("user.language");

    public static final String USER_NAME = getSystemProperty("user.name");

    public static final String USER_TIMEZONE = getSystemProperty("user.timezone");

    public static final String JAVA_VERSION_TRIMMED = getJavaVersionTrimmed();

    public static final float JAVA_VERSION_FLOAT = getJavaVersionAsFloat();

    public static final int JAVA_VERSION_INT = getJavaVersionAsInt();
    
    public static final boolean IS_JAVA_1_1 = getJavaVersionMatches("1.1");

    public static final boolean IS_JAVA_1_2 = getJavaVersionMatches("1.2");

    public static final boolean IS_JAVA_1_3 = getJavaVersionMatches("1.3");

    public static final boolean IS_JAVA_1_4 = getJavaVersionMatches("1.4");

    public static final boolean IS_JAVA_1_5 = getJavaVersionMatches("1.5");

    public static final boolean IS_JAVA_1_6 = getJavaVersionMatches("1.6");
    
    public static final boolean IS_JAVA_1_7 = getJavaVersionMatches("1.7");
    
    public static final boolean IS_JAVA_1_8 = getJavaVersionMatches("1.8");
    
    /**是否AIX系统*/
    public static final boolean IS_OS_AIX = getOSMatches("AIX");

    /**是否HP_UX系统*/
    public static final boolean IS_OS_HP_UX = getOSMatches("HP-UX");

    /**是否IRIX系统*/
    public static final boolean IS_OS_IRIX = getOSMatches("Irix");

    /**是否Linux系统*/
    public static final boolean IS_OS_LINUX = getOSMatches("Linux") || getOSMatches("LINUX");

    /**是否MAC系统*/
    public static final boolean IS_OS_MAC = getOSMatches("Mac");

    /**是否MAC_OSX系统*/
    public static final boolean IS_OS_MAC_OSX = getOSMatches("Mac OS X");

    /**是否OS2系统*/
    public static final boolean IS_OS_OS2 = getOSMatches("OS/2");

    /**是否Solaris系统*/
    public static final boolean IS_OS_SOLARIS = getOSMatches("Solaris");

    /**是否SUN系统*/
    public static final boolean IS_OS_SUN_OS = getOSMatches("SunOS");

    /**是否Unix系统*/
    public static final boolean IS_OS_UNIX =
        IS_OS_AIX || IS_OS_HP_UX || IS_OS_IRIX || IS_OS_LINUX ||
        IS_OS_MAC_OSX || IS_OS_SOLARIS || IS_OS_SUN_OS;

    /**是否Windows系列系统*/
    public static final boolean IS_OS_WINDOWS = getOSMatches(OS_NAME_WINDOWS_PREFIX);

    /**是否Windows 2000系统*/
    public static final boolean IS_OS_WINDOWS_2000 = getOSMatches(OS_NAME_WINDOWS_PREFIX, "5.0");

    /**是否Windows 95系统*/
    public static final boolean IS_OS_WINDOWS_95 = getOSMatches(OS_NAME_WINDOWS_PREFIX + " 9", "4.0");

    /**是否Windows 98系统*/
    public static final boolean IS_OS_WINDOWS_98 = getOSMatches(OS_NAME_WINDOWS_PREFIX + " 9", "4.1");

    /**是否Windows ME系统*/
    public static final boolean IS_OS_WINDOWS_ME = getOSMatches(OS_NAME_WINDOWS_PREFIX, "4.9");

    /**是否Windows NT系统*/
    public static final boolean IS_OS_WINDOWS_NT = getOSMatches(OS_NAME_WINDOWS_PREFIX + " NT");

    /**是否Windows XP系统*/
    public static final boolean IS_OS_WINDOWS_XP = getOSMatches(OS_NAME_WINDOWS_PREFIX, "5.1");

    /**是否Windows Vista系统*/
    public static final boolean IS_OS_WINDOWS_VISTA = getOSMatches(OS_NAME_WINDOWS_PREFIX, "6.0"); 

    public SystemUtils() {
        super();
    }

    /**
     * 返回JDK版本的单精度浮点数表现形式
     * 比如：
     * JDK版本号为1.2    返回1.2f
     * JDK版本号为1.3.1  返回1.31f
     * @return
     */
    private static float getJavaVersionAsFloat() {
        if (JAVA_VERSION_TRIMMED == null) {
            return 0f;
        }
        String str = JAVA_VERSION_TRIMMED.substring(0, 3);
        if (JAVA_VERSION_TRIMMED.length() >= 5) {
            str = str + JAVA_VERSION_TRIMMED.substring(4, 5);
        }
        try {
            return Float.parseFloat(str);
        } catch (Exception ex) {
            return 0;
        }
    }
    
    /**
     * 返回JDK版本的整数表现形式
     * 比如：
     * JDK版本号为1.2    返回120
     * JDK版本号为1.3.1  返回131
     * @return
     */
    private static int getJavaVersionAsInt() {
        if (JAVA_VERSION_TRIMMED == null) {
            return 0;
        }
        String str = JAVA_VERSION_TRIMMED.substring(0, 1);
        str = str + JAVA_VERSION_TRIMMED.substring(2, 3);
        if (JAVA_VERSION_TRIMMED.length() >= 5) {
            str = str + JAVA_VERSION_TRIMMED.substring(4, 5);
        } else {
            str = str + "0";
        }
        try {
            return Integer.parseInt(str);
        } catch (Exception ex) {
            return 0;
        }
    }

    private static String getJavaVersionTrimmed() {
        if (JAVA_VERSION != null) {
            for (int i = 0; i < JAVA_VERSION.length(); i++) {
                char ch = JAVA_VERSION.charAt(i);
                if (ch >= '0' && ch <= '9') {
                    return JAVA_VERSION.substring(i);
                }
            }
        }
        return null;
    }

    private static boolean getJavaVersionMatches(String versionPrefix) {
        if (JAVA_VERSION_TRIMMED == null) {
            return false;
        }
        return JAVA_VERSION_TRIMMED.startsWith(versionPrefix);
    }    
    
    private static boolean getOSMatches(String osNamePrefix) {
        if (OS_NAME == null) {
            return false;
        }
        return OS_NAME.startsWith(osNamePrefix);
    }    

    private static boolean getOSMatches(String osNamePrefix, String osVersionPrefix) {
        if (OS_NAME == null || OS_VERSION == null) {
            return false;
        }
        return OS_NAME.startsWith(osNamePrefix) && OS_VERSION.startsWith(osVersionPrefix);
    }    

    private static String getSystemProperty(String property) {
        try {
            return System.getProperty(property);
        } catch (SecurityException ex) {
            System.err.println("Caught a SecurityException reading the system property '" + property 
                + "'; the SystemUtils property value will default to null.");
            return null;
        }
    }
    
    public static boolean isJavaVersionAtLeast(float requiredVersion) {
        return JAVA_VERSION_FLOAT >= requiredVersion;
    }
    
    public static boolean isJavaVersionAtLeast(int requiredVersion) {
        return JAVA_VERSION_INT >= requiredVersion;
    }

    public static boolean isJavaAwtHeadless() {
        return JAVA_AWT_HEADLESS != null ? JAVA_AWT_HEADLESS.equals(Boolean.TRUE.toString()) : false;
    }
}
