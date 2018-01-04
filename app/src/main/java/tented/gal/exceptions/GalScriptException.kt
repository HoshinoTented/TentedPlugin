package tented.gal.exceptions

/**
 * GalScriptException
 * @author Hoshino Tented
 * @date 2018/1/5
 */

class GalScriptException( script : String , reason : String ) : Exception("Script $script went wrong because: $reason")