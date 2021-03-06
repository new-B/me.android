package me.java.library.utils.base.guava;

/**
 * File Name             :  AbstractEvent
 *
 * @author :  sylar
 * Create :  2019-10-01
 * Description           :
 * Reviewed By           :
 * Reviewed On           :
 * Version History       :
 * Modified By           :
 * Modified Date         :
 * Comments              :
 * CopyRight             : COPYRIGHT(c) me.iot.com   All Rights Reserved
 * *******************************************************************************************
 */
public abstract class AbstractEvent<Source, Content> {
    protected Source source;
    protected Content content;

    public AbstractEvent() {
    }

    public AbstractEvent(Content content) {
        this(null, content);
    }

    public AbstractEvent(Source source, Content content) {
        this.source = source;
        this.content = content;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

}
