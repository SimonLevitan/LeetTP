package cc.leet.leettp.util;

public class ToolBox {

    /**
     * Join a String[] into a single string with a joiner
     */
    public static String implode( String[] array, String glue ) {

        String out = "";

        if( array.length == 0 ) {
            return out;
        }

        for( String part : array ) {
            if(part == null) continue;
            out = out + part + glue;
        }
        out = out.substring(0, out.length() - glue.length() );

        return out;
    }

}
