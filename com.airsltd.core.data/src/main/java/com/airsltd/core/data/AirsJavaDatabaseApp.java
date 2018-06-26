/**
 *
 */
package com.airsltd.core.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.sql.Date;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import com.airsltd.core.NotificationStatus;
import com.airsltd.core.collections.AirsCollections;
import com.airsltd.core.data.converters.BlockConverters;

/**
 * Provide core code for a Java App that will connect to a database.
 *
 * @author Jon Boley
 *
 */
public class AirsJavaDatabaseApp {

	/**
	 * The number of collections that have occured before the creation of this instance.
	 */
	private long f_initialCollectionCount;
	private String[] f_args;
    private static final Map<String, char[]> SECURE_ATTRIBUTES = new HashMap<String, char[]>();
    //Ciphering (encryption and decryption) password/key.
    private static final char[] PASSWORD = "Unauthorized_Personel_Is_Unauthorized".toCharArray();
    //Cipher salt.
    private static final byte[] SALT = {
        (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12,
        (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12,};
    //Desktop dir:
    private File f_root = new File(".");
    private String f_encFile = "emailInfo.txt";

	public AirsJavaDatabaseApp(String[] p_args) {
		super();
		f_args = p_args;
		f_initialCollectionCount = getGcCount();
	}

	private static final int SECONDGROUP = 2;

	protected boolean globalCheck() {
		boolean l_retVal = false;
		if (switchExists(f_args, "--help", "-h")) {
			dumpHelpContent();
			l_retVal = true;
		}
		if (switchExists(f_args, "--version", "-v")) {
			dumpVersionContent();
			l_retVal = true;
		}
		return l_retVal;
	}


	/**
	 * Set the directory to store the encrypted data file in.
	 * <p>
	 * Used primarily for testing.
	 * In special cases this can be used to change the location of the security file.
	 * 
	 * @param p_root the root to set
	 */
	public void setRoot(File p_root) {
		f_root = p_root;
	}

	
	/**
	 * Set the file for storing encrypted properties.
	 * <p>
	 * Used primarily for testing.
	 * In special cases this can be used to change the name of the encrypted data file.
	 * 
	 * @param p_encFile the encrypted file to set
	 */
	public void setEncFile(String p_encFile) {
		f_encFile = p_encFile;
	}


	protected void dumpVersionContent() {
		System.out.println("Not Provided Yet");
	}


	/**
	 * Override this method to provide relevant help information for the application.
	 */
	public void dumpHelpContent() {
		System.out.println("This application does not provide any help information.");
	}


	/**
	 * Initialize the database and set up the ICoreInterface for the program.
	 *
	 * @param p_sqlConnection
	 */
	protected void initializeDatabase(ISqlConnection p_sqlConnection) {
		new CoreInterface();
		try {
			AirsPooledConnection.getInstance().initialize(p_sqlConnection);

		} catch (final ClassNotFoundException e) {
			CoreInterface.getSystem().handleException("Unable to initialize the Driver and Shared Pool", e,
					NotificationStatus.LOG);
		}
	}

	/**
	 * Determine if p_longId or p_shortId is contained in the array p_args.
	 *
	 * @param p_args
	 *            not null, an array containing all the arguments passed to this
	 *            application.
	 * @param p_longId
	 *            not null, the long id as a String (for example "-remote")
	 * @param p_shortId
	 *            not null, the short id as a String (for example "-r")
	 * @return
	 */
	public boolean switchExists(String[] p_args, String p_longId, String p_shortId) {
		boolean l_retVal = false;
		if (p_args.length > 0) {
			final List<String> l_args = Arrays.asList(p_args);
			l_retVal = l_args.contains(p_shortId);
			if (!l_retVal && p_longId!=null) {
				l_retVal = AirsCollections.testFor((String a) -> a.startsWith(p_longId), l_args, true);
			}
		}
		return l_retVal;
	}

	public String getArgData(String[] p_args, String p_equalId, String p_nextId) {
		String l_retVal = null;
		int l_index = 0;
		boolean l_break = false;
		for (final String l_current : p_args) {
			if (p_equalId != null && l_current.startsWith(p_equalId)) {
				l_retVal = l_current.substring(p_equalId.length());
				l_break = true;
			} else if (p_nextId != null && l_current.equals(p_nextId)) {
				l_retVal = p_args[l_index + 1];
				l_break = true;
			}
			if (l_break) {
				break;
			}
			l_index++;
		}
		return l_retVal;
	}
	
	public Date getArgDate(String[] p_args, String p_equalId, String p_nextId) throws ParseException {
		String l_value = getArgData(p_args, p_equalId, p_nextId);
		return l_value==null?null:BlockConverters.DATECONVERTER.fromSql(null, l_value);
	}

	public <T extends Enum<T>> Map<T, String> getArgHash(String p_string, String[] p_args, Class<T> p_class) {
		final Map<T, String> l_retVal = new HashMap<>();
		final Pattern l_pattern = Pattern.compile("([a-zA-Z]*)\\=(.*)");
		for (final String l_arg : p_args) {
			if (l_arg.startsWith(p_string)) {
				final Matcher l_match = l_pattern.matcher(l_arg.substring(p_string.length()));
				if (l_match.matches()) {
					final String l_name = l_match.group(1);
					final T l_enum = findEnum(p_class, l_name);
					l_retVal.put(l_enum, l_match.group(SECONDGROUP));
				}
			}
		}
		return l_retVal;
	}

	public Map<String, String> getArgHash(String p_string, String[] p_args) {
		final Map<String, String> l_retVal = new HashMap<>();
		final Pattern l_pattern = Pattern.compile("([a-zA-Z]*)\\=(.*)");
		for (final String l_arg : p_args) {
			if (l_arg.startsWith(p_string)) {
				final Matcher l_match = l_pattern.matcher(l_arg.substring(p_string.length()));
				if (l_match.matches()) {
					l_retVal.put(l_match.group(1), l_match.group(SECONDGROUP));
				}
			}
		}
		return l_retVal;
	}

	protected <T extends Enum<T>> T findEnum(Class<T> p_class, String p_string) {
		T l_retVal = null;
		final String l_string = p_string.toUpperCase();
		for (final T l_value : p_class.getEnumConstants()) {
			if (l_value.name().equals(l_string)) {
				l_retVal = l_value;
				break;
			}
		}
		return l_retVal;
	}

	/**
	 * Get the number of times all GC have been called.
	 * 
	 * @return the sum of all the times every Garbage Collector has been called.
	 */
	public long getGcCount() {
		long l_retVal = 0;
		for (GarbageCollectorMXBean b : ManagementFactory.getGarbageCollectorMXBeans()) {
			long l_count = b.getCollectionCount();
			if (l_count != -1) { l_retVal +=  l_count; }
		}
		return l_retVal;
	}
	
	/**
	 * Return the total memory in use.
	 * <p>
	 * Collect all unused memory and wait till the collection has completed.
	 * Then return the total amount of memory used.
	 * 
	 * @return long value of the memory in use.
	 */
	public long getReallyUsedMemory() {
		long l_before = getGcCount();
		System.gc();
		while (getGcCount() == l_before);
		return getCurrentlyUsedMemory();
	}
	
	/**
	 * Return the amount of memory in use by the jvm.
	 * <p>
	 * Total all the memory used in both Heap and Non Heap storage.
	 * 
	 * @return long value of the total memory usage.
	 */
	public long getCurrentlyUsedMemory() {
		return
				ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed() +
				ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getUsed();
	}
	
	/**
	 * Dump the number of GC calls and amount of Memory currently used.
	 * 
	 * @param p_stream  not null, the stream to dump this information to.
	 */
	public void dumpStastics(PrintStream p_stream) {
		p_stream.append("Current Memory and GC state");
		p_stream.println();
		long l_memoryUsed = getReallyUsedMemory();
		p_stream.append("Collections: ");
		p_stream.println(getGcCount()-f_initialCollectionCount);
		p_stream.append("Memory Used: ");
		p_stream.format("%.2f MB", l_memoryUsed/1000000f);
		p_stream.println();
	}
	
	/**
	 * Store a secure attribute.
	 * 
	 * @param p_who
	 * @param p_what
	 */
	public void setSecureAttribute(String p_who, char[] p_what) {
		SECURE_ATTRIBUTES.put(p_who, p_what);
	}
	
	/**
	 * Get the Secure attribute for p_who.
	 * 
	 * @param p_who
	 * @return
	 */
	public char[] getSecureAttribute(String p_who) {
		return SECURE_ATTRIBUTES.get(p_who);
	}
	
	/**
	 * Having finished using your secure attributes, clean them up.
	 */
	public void cleanseAttributes() {
		SECURE_ATTRIBUTES.clear();
		System.gc();
	}
	
	/**
	 * Read User Name and Password from stdin.
	 * <p>
	 * The values will be stored encrypted in {@link #f_encFile}.
	 * 
	 * @throws FileNotFoundException
	 * @throws GeneralSecurityException
	 * @throws IOException
	 */
	protected void setupPassword() throws FileNotFoundException, GeneralSecurityException, IOException {
		// save user name and password encrypted.
		
		System.out.print("User Name: ");

		Scanner l_scanIn = new Scanner(System.in);
		String l_userName = l_scanIn.nextLine();
		
		System.out.print("Password: ");
		String l_password = l_scanIn.nextLine();
		
		l_scanIn.close();           
		
        /*
         * Set secure attributes.
         * NOTE: Ignore the use of Strings here, it's being used for convenience only.
         * In real implementations, JPasswordField.getPassword() would send the arrays directly.
         */
        SECURE_ATTRIBUTES.put("Username", l_userName.toCharArray());
        SECURE_ATTRIBUTES.put("Password", l_password.toCharArray());
        createEncryptedFile(f_encFile, SECURE_ATTRIBUTES, 2);
		
	}

	protected void loadUser() throws FileNotFoundException, IOException, GeneralSecurityException {
		// load encrypted user name and password.
        //Descrypt first layer. (file content) (REMEMBER: Layers are in reverse order from writing).
		int l_lookup=2;
		if (switchExists(f_args, "-user=", null)) {
			SECURE_ATTRIBUTES.put("Username", getArgData(f_args, "-user=", null).toCharArray());
			l_lookup--;
		}
		if (switchExists(f_args, "-password=", null)) {
			SECURE_ATTRIBUTES.put("Password", getArgData(f_args, "-password=", null).toCharArray());
			l_lookup--;
		}
		if (l_lookup>0) {
			String decryptedContent = readFileApplyDecryption(f_encFile);
	        //Decrypt second layer (secure data).
	        for (String line : decryptedContent.split(System.lineSeparator())) {
	            String[] pair = line.split(": ", 2);
	            if (SECURE_ATTRIBUTES.get(pair[0])==null) {
	            	SECURE_ATTRIBUTES.put(pair[0], decrypt(pair[1]).toCharArray());
	            }
	        }
		}
	}

    protected String encrypt(byte[] property) throws GeneralSecurityException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PASSWORD));
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
        pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(SALT, 20));

        //Encrypt and save to temporary storage.
        String encrypted = Base64.getEncoder().encodeToString(pbeCipher.doFinal(property));

        cleanData(property);
        System.gc();

        //Return encryption result.
        return encrypted;
    }


	protected void cleanData(byte[] p_property) {
		//Cleanup data-sources - Leave no traces behind.
        for (int i = 0; i < p_property.length; i++) {
            p_property[i] = 0;
        }
        p_property = null;
	}

    protected String encrypt(char[] property) throws GeneralSecurityException {
        //Prepare and encrypt.
        byte[] bytes = new byte[property.length];
        for (int i = 0; i < property.length; i++) {
            bytes[i] = (byte) property[i];
        }
        String encrypted = encrypt(bytes);

        /*
         * Cleanup property here. (child data-source 'bytes' is cleaned inside 'encrypt(byte[])').
         * It's not being done because the sources are being used multiple times for the different layer samples.
         */
//      for (int i = 0; i < property.length; i++) { //cleanup allocated data.
//          property[i] = 0;
//      }
//      property = null; //de-allocate data (set for GC).
//      System.gc(); //Attempt triggering garbage-collection.

        return encrypted;
    }

    protected String encrypt(String property) throws GeneralSecurityException {
        String encrypted = encrypt(property.getBytes());
        /*
         * Strings can't really have their allocated data cleaned before CG,
         * that's why secure data should be handled with char[] or byte[].
         * Still, don't forget to set for GC, even for data of lesser importance;
         * You are making everything safer still, and freeing up memory as bonus.
         */
        property = null;
        return encrypted;
    }

    protected String decrypt(String property) throws GeneralSecurityException, IOException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PASSWORD));
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
        pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
        return new String(pbeCipher.doFinal(Base64.getDecoder().decode(property)));
    }

    protected void createEncryptedFile(
                    String p_fileName,
                    Map<String, char[]> p_secureAttributes,
                    int p_layers)
                    throws GeneralSecurityException, FileNotFoundException, IOException {
        StringBuilder l_sb = new StringBuilder();
        //First encryption layer. Encrypts secure attribute values only.
        for (String l_k : p_secureAttributes.keySet()) {
            String encryptedValue;
            if (p_layers >= 1) {
                encryptedValue = encrypt(p_secureAttributes.get(l_k));
            } else {
                encryptedValue = new String(p_secureAttributes.get(l_k));
            }
            l_sb.append(l_k).append(": ").append(encryptedValue).append(System.lineSeparator());
        }

        //Prepare file and file-writing process.
        File l_file = new File(f_root, p_fileName);
        if (!l_file.getParentFile().exists()) {
            l_file.getParentFile().mkdirs();
        } else if (l_file.exists()) {
            l_file.delete();
        }
        try (FileWriter l_fw = new FileWriter(l_file);
        		BufferedWriter l_bw = new BufferedWriter(l_fw)) {
        	//Second encryption layer. Encrypts whole file content including previously encrypted stuff.
        	if (p_layers >= 2) {
        		l_bw.append(encrypt(l_sb.toString().trim()));
        	} else {
        		l_bw.append(l_sb.toString().trim());
        	}
        	l_bw.flush();
        }
    }

    protected String readFileApplyDecryption(String p_fileName) throws FileNotFoundException, IOException, GeneralSecurityException {
    	Path l_path = new File(f_root, p_fileName).toPath();
		String l_sb = new String(Files.readAllBytes(l_path), StandardCharsets.UTF_8);
        return decrypt(l_sb.toString());
    }

}
