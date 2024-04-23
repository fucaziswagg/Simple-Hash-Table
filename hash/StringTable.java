package hash;

import java.util.LinkedList;

//
// STRINGTABLE.JAVA
// A hash table mapping Strings to their positions in the the pattern sequence
// You get to fill in the methods for this part.
//
public class StringTable {
    
    private LinkedList<Record>[] buckets;
    private int nBuckets;

    //
    // number of records currently stored in table --
    // must be maintained by all operations
    //
    public int size;
    
    
    //
    // Create an empty table with nBuckets buckets
    //
    @SuppressWarnings("unchecked")
	public StringTable(int nBuckets) {
    	this.nBuckets = nBuckets;
    	this.buckets = new LinkedList[nBuckets];
	
    	// TODO - fill in the rest of this method to initialize your table
    	this.size = 0;
    }
    
    
    /**
     * insert - inserts a record to the StringTable
     *
     * @param r
     * @return true if the insertion was successful, or false if a
     *         record with the same key is already present in the table.
     */
    public boolean insert(Record r)  {  
    	// TODO - implement this method
    	
    	// find key.    	
    	// search to see if already there
    	String key = r.key;
    	if (find(key) != null) {
    		return false;
    	}
    	// find hashcode & index?
    	int hashCode = stringToHashCode(key);
    	int bucketIndex = toIndex(hashCode);
    	if (bucketIndex >= this.nBuckets) {
    		return false;
    	}
    	//if no, first check to see if bucket has a linkedlist
    	if (buckets[bucketIndex] == null) {
    		buckets[bucketIndex] = new LinkedList<Record>();
    	}
    	buckets[bucketIndex].add(r);
    	size++;
    	return true;
    }
    
    
    /**
     * find - finds the record with a key matching the input.
     *
     * @param key
     * @return the record matching this key, or null if it does not exist.
     */
    public Record find(String key) {
    	// TODO - implement this method
    	int hashCode = stringToHashCode(key);
    	int bucketIndex = toIndex(hashCode);
    	if (bucketIndex >= this.nBuckets) {
    		return null;
    	}
    	if (buckets[bucketIndex] == null) {
    		return null;
    	}
    	for (Record r: buckets[bucketIndex]) {
    		if (r.key.equals(key)) {
    			return r;
    		}
    	}
    	
    	return null;
    }
    
    
    /**
     * remove - finds a record in the StringTable with the given key
     * and removes the record if it exists.
     *
     * @param key
     */
    public void remove(String key) {
    	// TODO - implement this method
    	Record r = find(key);
    	if (r == null) { return; }
    	int hashCode = stringToHashCode(key);
    	int bucketIndex = toIndex(hashCode);
    	buckets[bucketIndex].remove(r);
    	size--;
    }
    

    /**
     * toIndex - convert a string's hashcode to a table index
     *
     * As part of your hashing computation, you need to convert the
     * hashcode of a key string (computed using the provided function
     * stringToHashCode) to a bucket index in the hash table.
     *
     * You should use a multiplicative hashing strategy to convert
     * hashcodes to indices.  
     */
    private int toIndex(int hashcode)
    {
    	// Fill in your own hash function here
    	double a = (Math.sqrt(5)-1) / 2;
//    	System.out.println(a);
    	double formula = this.nBuckets*((hashcode*a)%1);
//    	System.out.println(formula);
    	formula = Math.floor(Math.abs(formula));
//    	System.out.println(formula);
//    	System.out.println((int) formula);
	
    	return (int) formula;
    }
    
    
    /**
     * stringToHashCode
     * Converts a String key into an integer that serves as input to
     * hash functions.  This mapping is based on the idea of integer
     * multiplicative hashing, where we do multiplies for successive
     * characters of the key (adding in the position to distinguish
     * permutations of the key from each other).
     *
     * @param string to hash
     * @returns hashcode
     */
    int stringToHashCode(String key)
    {
    	int A = 1952786893;
	
    	int v = A;
    	for (int j = 0; j < key.length(); j++)
	    {
    		char c = key.charAt(j);
    		v = A * (v + (int) c + j) >> 16;
	    }
	
    	return v;
    }

    /**
     * Use this function to print out your table for debugging
     * purposes.
     */
    public String toString() 
    {
    	StringBuilder sb = new StringBuilder();
	
    	for(int i = 0; i < nBuckets; i++) 
	    {
    		sb.append(i+ "  ");
    		if (buckets[i] == null) 
		    {
    			sb.append("\n");
    			continue;
		    }
    		for (Record r : buckets[i]) 
		    {
    			sb.append(r.key + "  ");
		    }
    		sb.append("\n");
	    }
    	return sb.toString();
    }
    
//    public static void main(String[] args) {
//    	
//    	StringTable st = new StringTable(5);
//    	String test = st.toString();
//    	System.out.println(test);
//    	Record r = new Record("cdefghix");
//    	st.insert(r);
//    	System.out.println(st.find("cdefghix"));;
//    	System.out.println(st.toString());
//    	int hashCode = st.stringToHashCode("cdefghix");
//    	int index = st.toIndex(hashCode);
//    	System.out.println("string: cdefghix. HashCode: " + hashCode + ".Index: " + index);
//    	
//    }
}
