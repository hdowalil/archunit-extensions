package info.fivecdesign.metrics;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/*
 * a List of these Nodes represents a Graph
 */
class Node {
	
	private String id = null;
	
	private Set<Node> connectedTo = null;

	Node(@Nonnull String id) {
		super();
		this.id = id;
		connectedTo = new HashSet<Node>();
	}
	
	static @Nullable Node findNode(@Nonnull final Collection<Node> nodes, @Nonnull final String nodeId) {
		return nodes.stream().filter(x -> x.getId().equals(nodeId)).findFirst().orElse(null);
	}
	
	String getId() {
		return id;
	}

	int connectTo(@Nonnull Node otherNode) {
		connectedTo.add(otherNode);
		return connectedTo.size();
	}
	
    boolean isPartOfACycle() {
    	
    	// we will try to reach ourself going down all subsequent dependencies
    	// if we cannot, this class is not part of a cycle
    	
    	Set<String> notVisitAgainHaveBeenThere = new HashSet<String>();
   		return findMyselfRecursive(connectedTo, notVisitAgainHaveBeenThere, this);
    }
    
    private boolean findMyselfRecursive (@Nonnull Collection<Node> dependencies, 
    		@Nonnull Set<String> notVisitAgainHaveBeenThere, @Nonnull Node node2Find) {
    	
        for (Node dependentNode : dependencies) {
        	
            String accessedNodeId = dependentNode.getId();
            
            if (accessedNodeId.equals(node2Find.getId())) {
            	// cycle!
            	return true;
            }
            
        	if (!notVisitAgainHaveBeenThere.contains(accessedNodeId)) {
        		
                notVisitAgainHaveBeenThere.add(accessedNodeId);
                boolean result = findMyselfRecursive(dependentNode.getConnectedTo(), notVisitAgainHaveBeenThere, node2Find);
                if (result) {
                	// cycle was found in recursive call
                	return true;
                }
        	}
        }

        // no cycle found!
        return false;
    }
    
	private Set<Node> getConnectedTo() {
		return connectedTo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		String ct = connectedTo.stream().map(n -> n.getId()).reduce("", String::concat);
		return "Node [id=" + id + ", connectedTo=" + ct + "]";
	}

	
}
