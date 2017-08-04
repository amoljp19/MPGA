package com.softaai.mpga.screens.smsall.mvcviews;

import com.softaai.mpga.sms.SmsMessage;
import com.softaai.mpga.screens.common.mvcviews.ViewMvc;

/**
 * This interface corresponds to a single SMS preview element (thumbnail) which is intended to
 * be used in a list of similar elements
 */
public interface SmsThumbnailViewMvc extends ViewMvc {

    /**
     * Show thumbnail of a particular SMS message
     * @param smsMessage the message to show
     */
    void bindSmsMessage(SmsMessage smsMessage);
}
