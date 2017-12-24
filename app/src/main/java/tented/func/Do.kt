package tented.func

/**
 * Created by Hoshino Tented on 2017/11/5.
 */
class Do
{
    companion object
    {
        fun serviceOnCreate()
        {
            //TODO
            //saki.demo.Demo.debug("[Service] Service was created")
        }

        private fun create()
        {
            //TODO
        }

        private fun destroy()
        {
            //TODO
        }

        fun onSet( bool : Boolean ) = if( bool ) create() else destroy()
    }
}