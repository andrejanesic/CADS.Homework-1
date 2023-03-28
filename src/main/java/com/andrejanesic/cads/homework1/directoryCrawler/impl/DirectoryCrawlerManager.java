package com.andrejanesic.cads.homework1.directoryCrawler.impl;

/**
 * Manages {@link DirectoryCrawler} threads.
 */
public class DirectoryCrawlerManager {

    /**
     * Singleton instance.
     * @return Singleton instance.
     */
    public static DirectoryCrawlerManager getInstance() {
        return DirectoryCrawlerSingleton.INSTANCE;
    }

    /**
     * Singleton holder, Bill Pugh implementation.
     * <a href="https://www.digitalocean.com/community/tutorials/java-singleton-design-pattern-best-practices-examples#5-bill-pugh-singleton-implementation">DigitalOcean documentation.</a>
     */
    private static class DirectoryCrawlerSingleton {
        private static final DirectoryCrawlerManager INSTANCE = new DirectoryCrawlerManager();
    }
}
