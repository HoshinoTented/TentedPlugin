package tented.gal

import tented.gal.exceptions.GalScriptException

/**
 * GalScript
 * @author Hoshino Tented
 * @date 2018/1/5
 *
 * Script grammar
 *
 * @title
 * Context
 * >choose
 * >choose
 * >......
 */

class GalScript private constructor( val title : String , val context : String , val chooses : Set<String> )
{
    companion object
    {
        fun newInstance( script : List<String> ) : GalScript
        {
            val iterator = script.iterator()

            if ( iterator.hasNext() )
            {
                var code : String = iterator.next()

                if (code.startsWith('@'))
                {
                    val title = code.substring(1)
                    val context = StringBuilder("")
                    val chooses = HashSet<String>()

                    while( iterator.hasNext() )
                    {
                        code = iterator.next()

                        if( ! code.startsWith('>') ) context.append(code) else chooses.add(code.substring(1))
                    }

                    return GalScript(title, context.toString(), chooses)
                }
                else throw GalScriptException(code, "The title not started with a '@'")
            }
            else throw GalScriptException("null", "The script is empty")
        }
    }

    override fun toString() : String = "[$title -> $context -> $chooses]"
}