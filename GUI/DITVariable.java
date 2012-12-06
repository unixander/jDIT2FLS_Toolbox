import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;


public class DITVariable {
	public String name, range, discret;
	public Double min,max; 
	private ArrayList<Double[][]> vars; //Terms values
	public List<String> terms; //Terms names
	
	public Double[][] getTerm(int index){
		if(vars.isEmpty()) return null;
		return vars.get(index);
	}
	/**
	 * Get array of the terms values
	 * @return array of the terms values
	 */
	public ArrayList<Double[][]> getAllTerms(){
		return vars;
	}
	/**
	 * Get terms quantity
	 * @return terms quantity
	 */
	public int getTermsQuantity(){
		return terms.size();
	}
	
	/**
	 * Add new term value
	 * @param current
	 */
	public void addTerm(Double[][] current){
		vars.add(current);
	}
	/**
	 * Modify term at the specified position
	 * @param index - position of the term
	 * @param current - new value
	 */
	public void updateTerm(int index,Double[][] current){
		vars.remove(index);
		vars.add(index, current);
	}
	
	/**
	 * Delete term at the specified position
	 * @param index 
	 */
	public void deleteTerm(int index){
		vars.remove(index);
	}
	
	public DITVariable(){
		name=new String();
		range=new String();
		discret=new String();
		terms=new ArrayList<String>();
		vars=new ArrayList<Double[][]>();
	};
	
	/**
	 * Calculate minimal and maximum elements from range
	 */
	private void calcDiscret(){
		String[] temp=range.split(";");
		try{
			min=Double.parseDouble(temp[0]);
			max=Double.parseDouble(temp[1]);
		} catch (Exception e){
			JOptionPane.showMessageDialog(null, "Wrong format of range field");
		}
	}
	
	/**
	 * Check if the term with the specified name already exists in the list of terms 
	 * @param name - terms name
	 * @param index - index of current term
	 * @return true - doesn't exist, false - already exist
	 */
	public boolean checkTermName(String name,int index){
		if(terms==null) return false;
		if(terms.indexOf(name)<0||(terms.indexOf(name)==index)) return true;
		return false;
	}
	
	/**
	 * Initialize variable with the name, range and disretization
	 * @param n - name
	 * @param r - range
	 * @param d - disretization
	 */
	public void Initialize(String n, String r, String d){
		if(n==null) return;
		name=n;
		if(r==null||d==null) return;
		if(r.isEmpty()||d.isEmpty()) return;
		range=r;
		discret=d;
		if(range.charAt(range.length()-1)!=';') range+=';';
		calcDiscret();
	}
}
