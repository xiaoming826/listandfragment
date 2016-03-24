package com.xiaoming.listandfragment;

import java.util.List;

/**
 * Created by xiaoming on 16/3/19.
 */
public class Bean {
    private String name;
    private List<SubBean> subBeans;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SubBean> getSubBeans() {
        return subBeans;
    }

    public void setSubBeans(List<SubBean> subBeans) {
        this.subBeans = subBeans;
    }

    public class SubBean  {
        private String name;
        private List<String> tags;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }
    }
}
