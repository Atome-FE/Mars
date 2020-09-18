package com.syc.suda.constant

import groovy.transform.CompileStatic

@CompileStatic
final class Constants {
    public static final String SESSION_KEY = "sessionInfo"
    public static final String TOKEN_KEY = "X-ADV-TOKEN"
    public static final String ENVIRONMENT = "ENVIRONMENT"
    public static final String DEF_DATE_FORMAT = "yyyy-MM-dd"
    public static final long DEFAULT_CASH_LOAN_AMOUNT_INTERVAL = 500_000

    /** Placeholder is {userId} */
    public static final String REPAYMENT_LOCK_KEY = "repayment:user:%s"


}
