package com.airsltd.core.data;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.airsltd.core.ICoreInterface;
import com.airsltd.core.data.AirsJavaDatabaseApp;
import com.airsltd.core.data.AirsPooledConnection;
import com.airsltd.core.data.CoreInterface;
import com.airsltd.core.data.ExceptionCollector;
import com.airsltd.core.data.ISqlConnection;
import com.airsltd.core.data.converters.BlockConverters;

public class AirsJavaDatabaseAppTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private AirsJavaDatabaseApp f_databaseConnection;

	@Before
	public void setUp() throws Exception {
		String[] l_string = new String[] { "-test" };
		f_databaseConnection = new AirsJavaDatabaseApp(l_string);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testInitializeDatabase() {
		// given
		ISqlConnection l_sqlConnection = mock(ISqlConnection.class);
		// when
		f_databaseConnection.initializeDatabase(l_sqlConnection);
		// then
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testSystemInterface() {
		// given
		ExceptionCollector<Object> l_collector = mock(ExceptionCollector.class);
		ISqlConnection l_sqlConnection = mock(ISqlConnection.class);
		f_databaseConnection.initializeDatabase(l_sqlConnection);
		// when
		ICoreInterface l_testInterface = CoreInterface.getSystem();
		// then
		assertNull(l_testInterface.getUserData());
		l_testInterface.loadProperty(new Object(), "", new Object());
		assertNull(l_testInterface.getProperty(new Object(), ""));
		assertFalse(l_testInterface.isProperty(""));
		l_testInterface.setPersistentProperty("", "");
		assertNull(l_testInterface.getPersistentProperty("", ""));
		assertFalse(l_testInterface.isPersistentProperty(""));
		assertNull(l_testInterface.productName());
		l_testInterface.playSound("", "");
		l_testInterface.pushExceptionCollector(l_collector);
		assertEquals(l_collector, l_testInterface.popExceptionCollector());
		assertFalse(l_testInterface.traceActive(AirsJavaDatabaseApp.class, ""));
		assertNull(l_testInterface.getApplication());
		assertEquals(DateFormat.getDateInstance(), l_testInterface.getDateFormat());
		
	}
	
	@Test
	public void testGetConnection() {
		AirsPooledConnection l_oldConnection = AirsPooledConnection.getInstance();
		try {
			// given
			AirsPooledConnection l_connection = mock(AirsPooledConnection.class);
			AirsPooledConnection.setInstance(l_connection);
			ISqlConnection l_sqlConnection = mock(ISqlConnection.class);
			f_databaseConnection.initializeDatabase(l_sqlConnection );
			Connection l_conn = mock(Connection.class);
			try {
				given(l_connection.getConnection()).willThrow(new SQLException()).willReturn(l_conn);
			} catch (SQLException e) {
				e.printStackTrace();
				assertTrue(false);
			}
			// when
			CoreInterface.getSystem().getConnection();
			Connection l_returnedConn = CoreInterface.getSystem().getConnection();
			// then
			assertEquals(l_conn, l_returnedConn);
		} finally {
			AirsPooledConnection.setInstance(l_oldConnection);
		}
	}
	
	@Test
	public void testSwitchExistsString() {
		// given
		String[] l_noArgs = new String[] {};
		String[] l_args = new String[] { "-test", "a", "switch", "--fail=continue" };
		// when
		// then
		assertFalse(f_databaseConnection.switchExists(l_noArgs, "-not", "-used"));
		assertFalse(f_databaseConnection.switchExists(l_args, null, null));
		assertTrue(f_databaseConnection.switchExists(l_args, "--fail", "junk"));
		assertFalse(f_databaseConnection.switchExists(l_args, "--fail3", "junk"));
		assertTrue(f_databaseConnection.switchExists(l_args, "--fail3", "-test"));
		assertTrue(f_databaseConnection.switchExists(l_args, "--fail3", "a"));
	}
	
	@Test
	public void testGetArgData() {
		// given
		String[] l_noArgs = new String[] {};
		String[] l_args = new String[] { "-test", "a", "switch", "--fail=continue" };
		// when
		// then
		assertEquals("a", f_databaseConnection.getArgData(l_args, "-test=", "-test"));
		assertEquals("a", f_databaseConnection.getArgData(l_args, null, "-test"));
		assertEquals(null, f_databaseConnection.getArgData(l_args, "-by", null));
		assertEquals(null, f_databaseConnection.getArgData(l_args, null, null));
		assertEquals(null, f_databaseConnection.getArgData(l_args, null, "-by"));
		assertEquals(null, f_databaseConnection.getArgData(l_noArgs, "-test=", "--fail="));
		assertEquals("continue", f_databaseConnection.getArgData(l_args,  "--fail=", "-by"));
		assertEquals("continue", f_databaseConnection.getArgData(l_args,  "--fail=", null));
	}
	
	@Test
	public void testGetArgDate() throws ParseException {
		// given
		String[] l_args = new String[] { "-test", "2018-05-04", "switch", "--fail=2017-01-01" };
		// when
		// then
		assertEquals(BlockConverters.DATECONVERTER.fromSql(null,"2018-05-04"), f_databaseConnection.getArgDate(l_args, null, "-test"));
		assertEquals(BlockConverters.DATECONVERTER.fromSql(null, "2017-01-01"), f_databaseConnection.getArgDate(l_args,  "--fail=", null));
		
	}
	
	@Test(expected=ParseException.class)
	public void testGetArgDateBad() throws ParseException {
		// given
		String[] l_args = new String[] { "-test", "notADate" };
		// when
		// then
		f_databaseConnection.getArgDate(l_args, null, "-test");
	}
	
	@Test
	public void testEncryptString() throws GeneralSecurityException {
		// given
		// when
		// then
		assertEquals("CR/muj1jMuh+bf4u+wa7NEWujfy2MJpY", f_databaseConnection.encrypt("ASeriousPassword"));
	}
	
	@Test
	public void testEncryptChar() throws GeneralSecurityException {
		// given
		// when
		// then
		assertEquals("CR/muj1jMuh+bf4u+wa7NEWujfy2MJpY", f_databaseConnection.encrypt("ASeriousPassword".toCharArray()));
		
	}
	
	@Test
	public void testDecryptString() throws GeneralSecurityException, IOException {
		// given
		// when
		// then
		assertEquals("ASeriousPassword", f_databaseConnection.decrypt("CR/muj1jMuh+bf4u+wa7NEWujfy2MJpY"));
	}
	
	@Test
	public void testCreateEncryptedFile() throws FileNotFoundException, GeneralSecurityException, IOException {
		// given
		Map<String, char[]> l_passwords = new HashMap<String, char[]>();
		l_passwords.put("Username", "ASpecificUser".toCharArray());
		l_passwords.put("Password", "ASeriousPassword".toCharArray());
		// when
		f_databaseConnection.createEncryptedFile("src/test/test.enc", l_passwords, 2);
		// then
		assertFileEqual("src/test/test.expected", "src/test/test.enc");
	}

	protected void assertFileEqual(String l_fileNameExpected, String l_fileNameReturned) throws IOException {
		String l_generated = new String(Files.readAllBytes(Paths.get(l_fileNameReturned)), StandardCharsets.UTF_8);
		String l_expected = new String(Files.readAllBytes(Paths.get(l_fileNameExpected)), StandardCharsets.UTF_8);
		assertEquals(l_expected,l_generated);
	}
	
	@Test 
	public void testReadFileApplyDecryption() throws FileNotFoundException, IOException, GeneralSecurityException {
		// given
		// when
		String l_decrypted = f_databaseConnection.readFileApplyDecryption("src/test/test.expected");
		// then
		assertEquals("Username: 7LTdp/3SLmTTGZAAgdNrxg=="+System.lineSeparator()+
				"Password: CR/muj1jMuh+bf4u+wa7NEWujfy2MJpY", l_decrypted);
	}
	
	@Test
	public void testSetupPassword() throws GeneralSecurityException, FileNotFoundException, IOException {
		// given
		InputStream l_oldStream = System.in;
		try {
			f_databaseConnection.setRoot(new File("src/test"));
			String l_testIn = "AUser\nASeriousPassword";
			InputStream l_inStream = new ByteArrayInputStream(l_testIn.getBytes());
			// when
			System.setIn(l_inStream);
			f_databaseConnection.setupPassword();
			assertFileEqual("src/test/emailInfo.expected", "src/test/emailInfo.txt");
		} finally {
			System.setIn(l_oldStream);
		}
	}
	
	@Test
	public void testLoadUser() throws FileNotFoundException, IOException, GeneralSecurityException {
		// given
		f_databaseConnection.setRoot(new File("src/test"));
		f_databaseConnection.setEncFile("emailInfo.expected");
		// when
		f_databaseConnection.loadUser();
		// then
		assertEquals("AUser", new String(f_databaseConnection.getSecureAttribute("Username")));
		assertEquals("ASeriousPassword", new String(f_databaseConnection.getSecureAttribute("Password")));
	}
	
}
