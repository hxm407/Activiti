/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.activiti.engine.test.pvm;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.impl.pvm.delegate.ExecutionListenerExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Tom Baeyens
 */
public class EventCollector implements ExecutionListener {
  
  private static final long serialVersionUID = 1L;

  private static Logger log = LoggerFactory.getLogger(EventCollector.class);
  
  public List<String> events = new ArrayList<String>(); 

  public void notify(DelegateExecution execution) {
    notify((ExecutionListenerExecution)execution);
  }
  
  public void notify(ExecutionListenerExecution execution) {
    
    log.debug("collecting event: {} on {}", execution.getEventName(), execution.getEventSource());
    events.add(execution.getEventName()+" on "+execution.getEventSource());
  }
  
  public String toString() {
    StringBuilder text = new StringBuilder();
    for (String event: events) {
      text.append(event);
      text.append("\n");
    }
    return text.toString();
  }
}
