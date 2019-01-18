package com.sanq.product.config.utils.ip;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by XieZhyan on 2018/8/6.
 */
public class LogFactory {

    private static final Logger logger;
    static {
        logger = Logger.getLogger("stdout");
        logger.setLevel(Level.ALL);
    }

    public static void log(String info, Level level, Throwable ex) {
        logger.log(level, info, ex);
    }

    public static Level  getLogLevel(){
        return logger.getLevel();
    }
}
