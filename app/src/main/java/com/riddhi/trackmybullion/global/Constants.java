package com.riddhi.trackmybullion.global;

/**
 * Created by riddhi on 8/7/2017.
 */

public class Constants {

    public static final int HTTP_OK = 200;
    public static final int REQUEST_CURRENCY_CONVERSION_ACTIVITY = 1;
    public static final int REQUEST_INVESMENTS_ACTIVITY = 2;
    public static final int REQUEST_INVESMENT_DETAIL_ACTIVITY = 3;
    public static final int REQUEST_ADD_INVESTMENT_ACTIVITY = 4;

    public static final String SELECTED_CURRENCY = "selected_currency";
    public static final String CURRENCIES = "currencies";
    public static final String NODE_INVESTMENTS = "investments";
    public static String DRAWABLE = "drawable";
    public static String COUNTRIES = "countries";

    public enum REQUEST_TYPE {GET, POST}
    public enum RETURN_TYPE {JSON, XML}

    private static final String quandl_api_key = "3VmfA2dyxFsYCuVmcFsz";
    public static enum HistoryRange {DAY, WEEK, MONTH, SIX_MONTH, YEAR, FIVE_YEAR, TEN_YEAR};

    /*
    * URLs : Urls for the service call
    * */
    private static final String host_name = "https://www.quandl.com";
    public static final String url_gold_prices = host_name+"/api/v3/datasets/BUNDESBANK/BBK01_WT5511.json"+
            "?api_key="+quandl_api_key;
    public static final String url_gold_prices_lsx = host_name+"/api/v3/datasets/LBMA/GOLD.json"+
            "?api_key="+quandl_api_key;
    public static final String url_silver_prices_lsx = host_name+"/api/v3/datasets/LBMA/SILVER.json"+
            "?api_key="+quandl_api_key;

    public static final float ounce = 28.3495f;

    /**
     * Urls for currency conversion
     */

    public static final String url_currency_rates = "http://api.fixer.io/latest";

    public enum CURRENCYCODE {AFA,
        ALL,
        DZD,
        ADP,
        AOA,
        ARS,
        AMD,
        AWG,
        ATS,
        AZM,
        BSD,
        BHD,
        BDT,
        BBD,
        BYB,
        RYR,
        BEF,
        BZD,
        BMD,
        BTN,
        BOB,
        BOV,
        BAM,
        BWP,
        BRL,
        BND,
        BGL,
        BGN,
        BIF,
        KHR,
        CAD,
        CVE,
        KYD,
        CLP,
        CLF,
        CNY,
        HKD,
        MOP,
        COP,
        KMF,
        CDF,
        CRC,
        HRK,
        CUP,
        CYP,
        CZK,
        DJF,
        DOP,
        TPE,
        IDE,
        ECS,
        ECV,
        EGP,
        SVC,
        ERN,
        EEK,
        ETB,
        XEU,
        EUR,
        FKP,
        FJD,
        FIM,
        XAF,
        GMD,
        GEL,
        DEM,
        GHC,
        GIP,
        GRD,
        DKK,
        GTQ,
        GNF,
        GWP,
        GYD,
        HTG,
        HNL,
        HUF,
        ISK,
        INR,
        IDR,
        XDR,
        IRR,
        IQD,
        IEP,
        ILS,
        JMD,
        JPY,
        JOD,
        KZT,
        KES,
        KPW,
        KRW,
        KWD,
        KGS,
        LAK,
        LVL,
        LBP,
        LSL,
        LRD,
        LYD,
        LTL,
        LUF,
        MKD,
        MGF,
        MWK,
        MYR,
        MVR,
        MTL,
        MRO,
        MUR,
        MXN,
        MXV,
        MDL,
        MNT,
        MZM,
        MMK,
        NAD,
        NPR,
        ANG,
        NLG,
        NIO,
        NGN,
        OMR,
        PKR,
        PAB,
        PGK,
        PYG,
        PEN,
        PHP,
        PLN,
        PTE,
        QAR,
        ROL,
        RUR,
        RUB,
        RWF,
        FRF,
        XCD,
        SHP,
        WST,
        ITL,
        STD,
        SAR,
        SCR,
        SLL,
        SGD,
        SKK,
        SIT,
        SBD,
        SOS,
        ZAR,
        ESP,
        LKR,
        SDP,
        SRG,
        NOK,
        SZL,
        SEK,
        CHF,
        SYP,
        TWD,
        TJR,
        TZS,
        THB,
        XOF,
        NZD,
        TOP,
        TTD,
        TND,
        TRL,
        TMM,
        AUD,
        UGX,
        UAH,
        AED,
        GBP,
        USS,
        USN,
        UYU,
        UZS,
        VUV,
        VEB,
        VND,
        USD,
        XPF,
        MAD,
        YER,
        YUN,
        ZRN,
        ZMK,
        ZWD
    };

}