package com.meiyou.usopp.data;

/**
 * Author: meetyou
 * Date: 17/9/19 17:00.
 */

public class TimerData {
    private Long mCost;
    private String mMethod;
    private boolean isThread;

    private TimerData(Builder builder) {
        setCost(builder.mCost);
        setMethod(builder.mMethod);
        setThread(builder.isThread);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public Long getCost() {
        return mCost;
    }

    public void setCost(Long cost) {
        mCost = cost;
    }

    public String getMethod() {
        return mMethod;
    }

    public void setMethod(String method) {
        mMethod = method;
    }

    public boolean isThread() {
        return isThread;
    }

    public void setThread(boolean thread) {
        isThread = thread;
    }

    /**
     * {@code TimerData} builder static inner class.
     */
    public static final class Builder {
        private Long mCost;
        private String mMethod;
        private boolean isThread;

        private Builder() {
        }

        /**
         * Sets the {@code mCost} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code mCost} to set
         * @return a reference to this Builder
         */
        public Builder withCost(Long val) {
            mCost = val;
            return this;
        }

        /**
         * Sets the {@code mMethod} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code mMethod} to set
         * @return a reference to this Builder
         */
        public Builder withMethod(String val) {
            mMethod = val;
            return this;
        }

        /**
         * Sets the {@code isThread} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code isThread} to set
         * @return a reference to this Builder
         */
        public Builder withIsThread(boolean val) {
            isThread = val;
            return this;
        }

        /**
         * Returns a {@code TimerData} built from the parameters previously set.
         *
         * @return a {@code TimerData} built with parameters of this {@code TimerData.Builder}
         */
        public TimerData build() {
            return new TimerData(this);
        }
    }
}
