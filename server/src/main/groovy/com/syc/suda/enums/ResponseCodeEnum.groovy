package com.syc.suda.enums

enum ResponseCodeEnum {
    //system
    SUCCESS("SUCCESS"),
    ERROR("Server error"),
    PARAMETER_ERROR("system.validate.error"),
    NOT_FOUND("system.error.not_found"),

    INVALID_CAPTCHA("Input captha is invalid, try again"),
    INVALID_AUTHORIZATION("Authorization information is invalid"),
    CAPTCHA_RESEND_FAILED("Captha resend failed"),
    AUTHORIZE_WEBSITES_FAILED("Get authorization websites failed, try again"),
    AUTHORIZE_LOGIN_PARAMS_FAILED("Can not get authorization, try again"),


    //account
    INVALID_INPUT("Bad request: Invalid inputs"),
    INVALID_NIK_CODE("Invalid nik number"),
    ACCOUNT_NOT_EXIST("This account is not exist"),
    ACCOUNT_EXIST("This account exist,try login"),
    LOGIN_FAILED("Username or password incorrect"),
    FACEBOOK_INVALID("Facebook user is invalid."),
    FACEBOOK_NOT_BINDING("Facebook ID doesn't bind to user."),
    FACEBOOK_BINDING_EXSIT("Facebook user has been bounded."),
    USER_BINDING_EXIST("User has bounded to facebook account."),
    ACCOUNT_KIT_INVALID("Account kit token invalid."),

    @Deprecated
    PASSWORD_INCORRECT("password incorrect"),
    @Deprecated
    NO_LOGIN("The user is not logged in"),


    //auth
    UNAUTHORIZED("You are not authorized to access this page"),
    TOKEN_EXPIRED("Auth token expired,try login please."),
    TOO_MANY_REQUESTS("Too many requests"),
    EMPTY_PARAMETER_ERROR("Empty parameters are not allowed"),
    BATCH_LIMIT_EXCEEDED("The maximum limit for batch request has been exceeded"),

    OTP_OUT_OF_LIMITS("Send sms limited"),
    SMS_WRONG_VERIFICATION_CODE('Wrong sms code'),
    AUDIT_STATUS("Credit application\'s status is invalid"),

    // boss admin user
    USER_NOT_FOUND("User not found"),
    USER_DISABLED("User is disabled"),

    INVALID_IMAGE('Invalid image'),

    //credit
    CREDIT_NOT_EXIST("Credit not exist"),

    //invite code
    INVALID_INVITE_CODE("Invalid invite code"),
    USER_ALREADY_HAS_APPLICATION("User already has application"),
    USER_HAS_BEEN_INVITED("User has been invited"),

    //merchandise
    CREDIT_UNRATED("Credit Unrated"),
    CREDIT_BLOCKED("Credit Blocked"),
    CREDIT_FROZEN("Credit Frozen"),
    CREDIT_USED("Credit Used"),
    CREDIT_RELEASED("Credit was unfreeze before loan"),
    FREEZE_ZERO_AMOUNT("Freeze Zero Amount"),
    INSUFFICIENT_CREDIT("Insufficient Credit"),
    INVALID_REPAYMENT_PLAN("Invalid Repayment Plan"),
    TRANSACTION_NOT_EXIST('Transaction Not Exist'),
    LOAN_REPAID('loan has repaid record, could not cancel'),
    INCORRECT_LOAN_STATUS('incorrect status for loan to cancel'),
    COUPON_CANNOT_BIND("Coupon can't bind to user"),
    COUPON_EXCHANGE_FAILED("Fail to exchange coupon for user"),
    REFUND_FAILED("loan cannot be refunded"),
    LOAN_NOT_EXIST("Loan not exist"),

    //special code
    //Special Code Not Exists
    SPECIAL_CODE_NOT_EXISTS("Maaf, kode tidak tersedia"),
    //Invalid Special Code
    INVALID_SPECIAL_CODE("Maaf, kode tidak tersedia"),

    //bank account
    BANK_ACCOUNT_APPLICATION_INVALID("User has checking or declined bank account application"),

    // ab-test
    USER_NOT_MATCH("sorry, you can't operate other user id"),

    //user info valid
    USER_CONTACT_NAME_CONFLICT("contact name is conflict"),

    OTP_INCORRECT("otp code is incorrect"),

    NEED_OTP_CHECK("otp code is needed"),

    UNABLE_TO_LOAN("unable to loan"),

    ALREADY_ADD_CONTACT("Already Add Additional Contact Before"),

    CONTACT_FULL_NAME_EXIST("Contact full name already exist."),
    CONTACT_MOBILE_NUMBER_EXIST("Contact mobile number already exist."),

    //channel
    INVALID_CHANNEL("Channel is invalid"),

    //person-info
    INVALID_PHONE_NO("Phone number is invalid"),
    INVALID_FULL_NAME("Full name is invalid"),
    INVALID_CONTACT_PHONE_NUMBER("Contact phone number is invalid"),
    INVALID_ZIP_CODE("Zip code is invalid"),
    INVALID_PHOTO("Photo is invalid"),

    INVALID_CHECKOUTID("Invalid checkoutId")


    String message

    ResponseCodeEnum(String message) {
        this.message = message
    }

    String getCode() {
        return name()
    }

}