package saiday.myaudaiocast.myaudiocast.audio;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
/**
 * Created by saiday on 15/6/6.
 */
public class PhoneListener extends PhoneStateListener {
    public interface IncomingCallListener {
        void incomingCallStarted();
    }

    private IncomingCallListener mIncomingCallListener;

    public PhoneListener(IncomingCallListener incomingCallListener) {
        mIncomingCallListener = incomingCallListener;
    }

    public void onCallStateChanged(int state, String incomingNumber) {
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                mIncomingCallListener.incomingCallStarted();
                break;
            default:
                break;
        }
    }

}
