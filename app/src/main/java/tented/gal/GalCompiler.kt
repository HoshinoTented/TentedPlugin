package tented.gal

/**
 * GalCompiler
 * @author Hoshino Tented
 * @date 2018/1/5
 */

class GalCompiler constructor( val scripts : Set<GalScript> )
{
    companion object
    {
        private fun toList( source : String ) : List<String> = source.split('\n')

        fun newInstance( script : String ) : GalCompiler
        {
            val scripts = HashSet<GalScript>()

            val lines = toList(script)
            val iterator = lines.iterator()

            while( iterator.hasNext() )
            {
                var line = iterator.next()

                if( line.startsWith('@') )
                {
                    val scriptLines = ArrayList<String>()

                    scriptLines.add(line)

                    while( iterator.hasNext() )
                    {
                        line = iterator.next()

                        if( line != "" ) scriptLines.add(line + "\n") else break
                    }

                    scripts.add(GalScript.newInstance(scriptLines))
                }
            }

            return GalCompiler(scripts)
        }
    }

    inline fun handle( input : String , handleLambda : (GalScript) -> Unit )
    {
        for( script in scripts ) if( input == script.title ) handleLambda(script)
    }

    override fun toString() : String = scripts.toString()
}