package com.andrejanesic.cads.homework1.job.type;

import com.andrejanesic.cads.homework1.job.IJob;
import com.andrejanesic.cads.homework1.job.JobType;
import lombok.Getter;

public abstract class IWebJob extends IJob {

    /**
     * URL address of the web page that should be scanned.
     */
    @Getter
    private final String url;

    /**
     * Allowed number of hops.
     */
    @Getter
    private final int hops;

    /**
     * Web page scanning job.
     *
     * @param url  URL address of the web page that should be scanned.
     * @param hops Allowed number of hops.
     */
    public IWebJob(String url, int hops) {
        super(JobType.WEB);
        this.url = url;
        this.hops = hops;
    }
}
