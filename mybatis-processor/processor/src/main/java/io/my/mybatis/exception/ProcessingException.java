package io.my.mybatis.exception;

import javax.lang.model.element.Element;

public class ProcessingException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    Element element;

    public ProcessingException(Element element, String msg, Object... args) {
      super(String.format(msg, args));
      this.element = element;
    }

    public Element getElement() {
      return element;
    }
}
