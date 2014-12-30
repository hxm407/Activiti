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

import org.activiti.engine.test.pvm.activities.Automatic;
import org.activiti.engine.test.pvm.activities.End;
import org.activiti.engine.test.pvm.activities.WaitState;
import org.activiti5.engine.impl.pvm.ProcessDefinitionBuilder;
import org.activiti5.engine.impl.pvm.PvmExecution;
import org.activiti5.engine.impl.pvm.PvmProcessDefinition;
import org.activiti5.engine.impl.pvm.PvmProcessInstance;
import org.activiti5.engine.impl.test.PvmTestCase;


/**
 * @author Tom Baeyens
 */
public class PvmScopeWaitStateTest extends PvmTestCase {

  /**
   * +-----+   +----------+   +---+
   * |start|-->|scopedWait|-->|end|
   * +-----+   +----------+   +---+
   */
  public void testWaitStateScope() {
    PvmProcessDefinition processDefinition = new ProcessDefinitionBuilder()
      .createActivity("start")
        .initial()
        .behavior(new Automatic())
        .transition("scopedWait")
      .endActivity()
      .createActivity("scopedWait")
        .scope()
        .behavior(new WaitState())
        .transition("end")
      .endActivity()
      .createActivity("end")
        .behavior(new End())
      .endActivity()
    .buildProcessDefinition();
    
    PvmProcessInstance processInstance = processDefinition.createProcessInstance();
    processInstance.start();
    
    PvmExecution execution = processInstance.findExecution("scopedWait");
    assertNotNull(execution);
    
    execution.signal(null, null);
  
    assertEquals(new ArrayList<String>(), processInstance.findActiveActivityIds());
    assertTrue(processInstance.isEnded());
  }

  /**
   *          +--------------+ 
   *          | outerScope   |
   * +-----+  | +----------+ |  +---+
   * |start|--->|scopedWait|--->|end|
   * +-----+  | +----------+ |  +---+
   *          +--------------+ 
   */
  public void testNestedScope() {
    PvmProcessDefinition processDefinition = new ProcessDefinitionBuilder()
      .createActivity("start")
        .initial()
        .behavior(new Automatic())
        .transition("scopedWait")
      .endActivity()
      .createActivity("outerScope")
        .scope()
        .createActivity("scopedWait")
          .scope()
          .behavior(new WaitState())
          .transition("end")
        .endActivity()
      .endActivity()
      .createActivity("end")
        .behavior(new End())
      .endActivity()
    .buildProcessDefinition();
    
    PvmProcessInstance processInstance = processDefinition.createProcessInstance();
    processInstance.start();
    
    PvmExecution activityInstance = processInstance.findExecution("scopedWait");
    assertNotNull(activityInstance);
    
    activityInstance.signal(null, null);
  
    assertEquals(new ArrayList<String>(), processInstance.findActiveActivityIds());
    assertTrue(processInstance.isEnded());
  }
}
