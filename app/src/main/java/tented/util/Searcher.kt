package tented.util

/**
 * Searcher
 * @author Hoshino Tented
 * @date 2018/1/13 17:16
 */
interface Searcher
{
    fun format( page : String ) : String
    fun search( keyWord : String ) : String
}