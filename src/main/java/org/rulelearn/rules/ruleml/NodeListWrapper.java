/**
 * Copyright (C) Jerzy Błaszczyński, Marcin Szeląg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.rulelearn.rules.ruleml;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.AbstractList;
import java.util.RandomAccess;

/**
 * Wrapper of {@link NodeList}, which makes it iterable.
 *
 * @author Jerzy Błaszczyński (<a href="mailto:jurek.blaszczynski@cs.put.poznan.pl">jurek.blaszczynski@cs.put.poznan.pl</a>)
 * @author Marcin Szeląg (<a href="mailto:marcin.szelag@cs.put.poznan.pl">marcin.szelag@cs.put.poznan.pl</a>)
 * 
 */
public class NodeListWrapper extends AbstractList<Node> implements RandomAccess {
	/**
	 * Wrapped list {@link NodeList}
	 */
    private final NodeList list;

    /**
     * Creates a wrapper based on the given {@link NodeList}
     * @param list list to wrap
     */
    public NodeListWrapper(NodeList list) {
        this.list = list;
    }

    @Override
    public Node get(int index) {
        return list.item(index);
    }

    @Override
    public int size() {
        return list.getLength();
    }
}