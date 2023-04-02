package com.andrejanesic.cads.homework1.job;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

/**
 * Wraps the required information to start a scanning job.
 */
@RequiredArgsConstructor
public abstract class IJob {

    /**
     * Unique, random ID of the job.
     */
    @Getter
    private final String id = System.currentTimeMillis() + ""
            + (int) (Math.random() * 1000);
    /**
     * Type of the job.
     */
    @Getter
    @NonNull
    private final JobType type;
    /**
     * The job from which this job originated.
     */
    @Getter
    private IJob parent;
    /**
     * Children jobs which occurred from processing this job. Should not be
     * modified.
     */
    @Getter
    private Set<IJob> children = new HashSet<>();
    /**
     * Patterns for granular matching. TODO not yet implemented in scanners.
     */
    @Getter
    @Setter
    private Set<Pattern> patterns = new HashSet<>();
    /**
     * Result of the job.
     */
    @Getter
    @Setter
    private Future<Map<String, Integer>> result;

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof IJob) &&
                ((IJob) obj).getId().equals(getId()) &&
                ((IJob) obj).getType().equals(getType());
    }

    /**
     * Sets the parent job and adds it as a child.
     *
     * @param job parent job
     */
    public void setParent(IJob job) {
        if (this.parent != null) {
            parent.removeChild(this);
        }
        this.parent = job;
        if (job != null) {
            job.addChild(this);
        }
    }

    /**
     * Removes the parent from this job.
     */
    public void removeParent() {
        if (parent == null) return;
        setParent(null);
    }

    /**
     * Adds a child job and sets its parent to this.
     *
     * @param job job to add to children
     */
    public void addChild(IJob job) {
        if (job == null) return;
        if (children.contains(job)) return;
        children.add(job);
        job.parent = this;
    }

    /**
     * Removes a child job and sets its parent to null.
     *
     * @param job job to remove from children
     */
    public void removeChild(IJob job) {
        if (job == null) return;
        if (!children.contains(job)) return;
        children.remove(job);
        if (job.parent != null && job.parent.equals(this)) {
            job.parent = null;
        }
    }
}
