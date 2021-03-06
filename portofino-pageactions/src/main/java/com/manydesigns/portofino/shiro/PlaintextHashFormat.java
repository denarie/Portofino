package com.manydesigns.portofino.shiro;

import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.crypto.hash.format.HashFormat;

/**
 * @author Angelo Lupo          - angelo.lupo@manydesigns.com
 * @author Giampiero Granatella - giampiero.granatella@manydesigns.com
 * @author Emanuele Poggi       - emanuele.poggi@manydesigns.com
 * @author Alessio Stalla       - alessio.stalla@manydesigns.com
 */
public class PlaintextHashFormat implements HashFormat {
    public static final String copyright =
            "Copyright (C) 2005-2017 ManyDesigns srl";

    @Override
    public String format(Hash hash) {
        return new String(hash.getBytes());
    }
}
