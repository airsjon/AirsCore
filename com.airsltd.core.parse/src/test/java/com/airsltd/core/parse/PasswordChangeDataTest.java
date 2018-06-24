package com.airsltd.core.parse;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.airsltd.core.IAirsStatusMessage;

public class PasswordChangeDataTest {

	private PasswordChangeData f_passwordChangeData;

	@Before
	public void setUp() throws Exception {
		f_passwordChangeData = new PasswordChangeData("mockUser");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGettersSetters() {
		// given
		// when
		f_passwordChangeData.setNewPassword("newPassword");
		f_passwordChangeData.setOldPassword("oldPassword");
		f_passwordChangeData.setUser("newMockUser");
		f_passwordChangeData.setVerifiedPassword("verifiedPassword");
		// then
		assertEquals("newPassword", f_passwordChangeData.getNewPassword());
		assertEquals("oldPassword", f_passwordChangeData.getOldPassword());
		assertEquals("newMockUser", f_passwordChangeData.getUser());
		assertEquals("verifiedPassword", f_passwordChangeData.getVerifiedPassword());
	}
	
	@Test
	public void testValidInput() {
		// given
		// when
		f_passwordChangeData.setNewPassword(null);
		f_passwordChangeData.setVerifiedPassword(null);
		List<IAirsStatusMessage> l_retOne = f_passwordChangeData.validInput();
		f_passwordChangeData.setVerifiedPassword("notTheSame");
		List<IAirsStatusMessage> l_retTwo = f_passwordChangeData.validInput();
		f_passwordChangeData.setNewPassword("short");
		List<IAirsStatusMessage> l_retThree = f_passwordChangeData.validInput();
		f_passwordChangeData.setNewPassword("itIsLongEnough");
		f_passwordChangeData.setVerifiedPassword("itIsLongEnough");
		assertNull(f_passwordChangeData.validInput());
		// then
		assertEquals(1, l_retOne.size());
		assertEquals(2, l_retTwo.size());
		assertEquals(2, l_retThree.size());
	}

}
