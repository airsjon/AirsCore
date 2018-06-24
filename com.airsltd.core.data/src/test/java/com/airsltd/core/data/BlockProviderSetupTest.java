/**
 * 
 */
package com.airsltd.core.data;

import static org.mockito.BDDMockito.*;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.airsltd.core.ICoreInterface;
import com.airsltd.core.NotificationStatus;
import com.airsltd.core.data.CoreInterface;

/**
 * @author jon_000
 *
 */
public class BlockProviderSetupTest {
	
	/**
	 * Debugging constant that can be set to show stack traces for all exceptions passed to the ICoreInterface handler.
	 */
	protected static final boolean SHOWTRACEALWAYS = false;
	private static Answer<Boolean> s_exceptionAnswer = new Answer<Boolean>() {

		@Override
		public Boolean answer(InvocationOnMock p_invocation)
				throws Throwable {
			String l_reason = ((String) p_invocation.getArguments()[0]);
			Throwable l_throw = ((Throwable)p_invocation.getArguments()[1]);
			Exception l_excpetion = new RuntimeException(l_reason, l_throw);
			if (SHOWTRACEALWAYS) {
				if (l_throw != null) {
					l_throw.printStackTrace();
				} else {
					l_excpetion.printStackTrace();
				}
			}
			throw(l_excpetion);
		}
	};

	/**
	 * @throws java.lang.Exception
	 */
	public void setUp() throws Exception {
		CoreInterface.setSystem(mock(ICoreInterface.class));
		given(CoreInterface.getSystem().
				handleException(anyString(), any(Throwable.class), any(NotificationStatus.class))).will(s_exceptionAnswer);
	}

}
