package org.activiti.engine.impl.bpmn.behavior;

import org.activiti.bpmn.model.Message;
import org.activiti.engine.delegate.DelegateExecution;

public interface ThrowMessageJavaDelegate {

    Object execute(DelegateExecution execution, Message message);

}
