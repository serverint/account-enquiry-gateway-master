package com.wasp.gateway.enquiry.account
/**
 * Created by aalexandrakis on 26/04/2017.
 */

public enum ERROR_CODES {
    internal_error, invalid_request, instructed_bank_not_found, instructed_bank_url_not_found, constraint_violation
}

public enum CURRENCIES {
    NGN
}

public enum CHARGE_BEARER_VALUES {
    DEBT, CRED, SHAR, SLEV
}

public enum CATEGORY_PURSPOSE_VALUES {
    BONU, CASH, CBLK, CCRD, CORT, DCRD, DIVI, DVPM, EPAY, FCOL, GOVT, HEDG, ICCP, IDCP, INTC, INTE, LOAN, OTHR, PENS, RVPM, SALA, SECU, SSBE, SUPP, TAXS, TRAD, TREA, VATX, WHLD
}

public enum PRIORITY_VALUES {
    BKTR, NUGP, NURG, PRPT, SDVA, URGP, URNS
}


