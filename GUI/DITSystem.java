import im1.UnitIM1;
import im2.UnitIM2;
import im3.UnitIM3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import lio.IOLV;

import rules.DBRules;

import defuzzifier.Defuzzification;
import ekm.TypeReducer;
import fuzzifier.Fuzzification;


public class DITSystem {
	//name,LOpath,LIpath,Rulepath,modelPath - variable for saving system
	public String name=new String(), LOpath=new String(), 
			LIpath=new String(), Rulepath=new String(),
			modelPath=new String();
	public List<DITVariable> LIvars, LOvars;	//list of variables
	public DITRule rule;						//Rules database
	private ArrayList<Double[][]>[] LI,LO;		//Variable for library functions
	private Double[][] R;						//Rules database for library functions
	@SuppressWarnings("unused")
	private Double result;						//Result of calculations
	private UnitIM1 im1 = new UnitIM1();        //IM1
    private UnitIM2 im2 = new UnitIM2();        //IM2        
    private UnitIM3 im3 = new UnitIM3();		//IM3
    private Fuzzification fuzz =new Fuzzification(); //Fuzzification
    private DBRules rs = new DBRules(); 		//Rules database I\O
    private IOLV lio=new IOLV("LI");			//Variables I\O

	public DITSystem(){
		LIvars=new ArrayList<DITVariable>();
		LOvars=new ArrayList<DITVariable>();
		rule=new DITRule();
	};
	
	/**
	 * Load Linguistic input variable from file path
	 * @param path - path to file with LI variables
	 * @return	variables structure for library
	 */
	public ArrayList<Double[][]>[] loadLI(String path){
		lio.changeMode("LI");
		try{
			LI=lio.ReadFromXml(path);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
			LI=null;
		}
		this.convertDatatoInner(0);
		return LI;
	}
	
	/**
	 * Load Linguistic output variable from file path
	 * @param path - path to file with LO variables
	 * @return	variables structure for library
	 */
	public ArrayList<Double[][]>[] loadLO(String path){
		lio.changeMode("LO");
		try{
			LO=lio.ReadFromXml(path);
		} catch (Exception e){
			JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
			LO=null;
		}
		this.convertDatatoInner(1);
		return LO;
	}
	
	/**
	 * Load Rules from file path
	 * @param path - path to file with rules
	 * @return	rules structure for library
	 */
	public Double[][] loadRules(String path){
		try{
			R=rs.ReadFromXml(path);
		} catch(Exception e){
			JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
			R=null;
		}
		this.convertDatatoInner(2);
		return R;
	}
	
	/**
	 * Save Linguistic input variable from file path
	 * @param path - path to file with LI variables
	 */
	public void saveLI(String path){
		this.convertDatatoOutter(0);
		lio.changeMode("LI");
		try{
			lio.SaveToXml(path, LI);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "An error occured. Please, check all parameters of LI variable","Error",JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Save Linguistic output variable from file path
	 * @param path - path to file with LO variables
	 */
	public void saveLO(String path){
		this.convertDatatoOutter(1);
		lio.changeMode("LO");
		try{
			lio.SaveToXml(path, LO);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "An error occured. Please, check all parameters of LO variable","Error",JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Save Rules from file path
	 * @param path - path to file with rules
	 */
	public void saveRules(String path){
		this.convertDatatoOutter(2);
		try{
			rs.SaveToXml(path, R);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "An error occured. Please, check all parameters of Rules base","Error",JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Save system configuration to xml file
	 * @param path - path to target xml file
	 */
	public void saveSystemConfiguration(String path){
		try{
			checkTargetDirectory(path);
			OutputStream outputStream = new FileOutputStream(new File(path+"\\config.xml"));

			XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(
			                new OutputStreamWriter(outputStream, "utf-8"));
			out.writeStartDocument("utf-8", "1.0");
			out.writeCharacters("\n");
			out.writeStartElement("Configuration");
			out.writeCharacters("\n");
			out.writeStartElement("Name");
			out.writeCharacters(this.name);
			out.writeEndElement();
			out.writeCharacters("\n");
			out.writeEndElement();
			out.writeCharacters("\n");
			out.writeEndDocument();
			out.close();
			outputStream.close();
		} catch (Exception ex){
			System.out.println(ex.getLocalizedMessage());
            ex.printStackTrace();
		} 
	}
	
	public void checkTargetDirectory(String path){
		try{
			File target=new File(path);
			if(!target.exists()){
				target.mkdir();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	/**
	 * Load system configuration to xml file
	 * @param path - path to target xml file
	 */
	public void loadSystemConfiguration(String path){
		try{
			SAXParserFactory saxParserFactory=SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();
			
			DefaultHandler defaultHandler = new DefaultHandler(){
				boolean bName=false;
				
				public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException{
					if(qName.equalsIgnoreCase("name")){
                        bName=true;
                    }
                }
				public void characters(char ch[], int start, int length) throws SAXException{
					String currentString=new String(ch, start, length);
                    if(bName){
                        name=currentString;
                        bName = false;
                    }
                }
			};
			File file = new File(path+"\\config.xml");
			if(!file.exists()) return;
			InputStream inputStream = new FileInputStream(file);
			Reader reader = new InputStreamReader(inputStream, "UTF-8");
			 
			InputSource inputSource = new InputSource(reader);
			inputSource.setEncoding("UTF-8");
			 
			saxParser.parse(inputSource, defaultHandler);
			inputStream.close();
		} catch (Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Configuration file format is invalid");
        }
	}
	
	/**
	 * Converts variables lists to the inner format of the GUI application
	 */
	public void convertDatatoInner(int param){
		String[] res=null;
		if(LI!=null&&(param==0||param==-1)){
			LIvars=new ArrayList<DITVariable>();
			int i=0;
			res=getRange(LI[0].get(0));
			for(ArrayList<Double[][]> var: LI){
				DITVariable temp=new DITVariable();
				temp.Initialize("InputVar"+i, res[1], res[0]);
				int j=0;
				for(Double[][] term: var){
					temp.terms.add("Term"+j);
					temp.addTerm(term);
					j++;
				}
				i++;
				LIvars.add(temp);
			}
			LI=null;
		}
		if(LO!=null&&(param==1||param==-1)){
			LOvars=new ArrayList<DITVariable>();
			int i=0;
			res=getRange(LO[0].get(0));
			for(ArrayList<Double[][]> var: LO){
				DITVariable temp=new DITVariable();
				temp.Initialize("OutputVar"+i, res[1], res[0]);
				int j=0;
				for(Double[][] term: var){
					temp.terms.add("Term"+j);
					temp.addTerm(term);
					j++;
				}
				i++;
				LOvars.add(temp);
			}
			LO=null;
		}
		if(R!=null&&(param==2||param==-1))
			if(R.length>0){
				rule=new DITRule();
				rule.rules=R;
				R=null;
			}
	}
	private String[] getRange(Double[][] in){
		String[] result=new String[2];
		result[0]=Double.toString(in[1][0]-in[0][0]);
		result[1]=Double.toString(in[0][0])+";"+Double.toString(in[in.length-1][0])+";";
		return result;
	}
	/**
	 * Converts variables lists to the format of the jDIT2FLS library
	 */
	@SuppressWarnings("unchecked")
	public void convertDatatoOutter(int param){
		if(LIvars!=null&&(param==0||param==-1)){
			LI=new ArrayList[LIvars.size()];
			int i=0;
			for(DITVariable var: LIvars){
				LI[i]=var.getAllTerms();
				i++;
			}
		}
		if(LOvars!=null&&(param==1||param==-1)){
			LO=new ArrayList[LOvars.size()];
			int i=0;
			for(DITVariable var: LOvars){
				LO[i]=var.getAllTerms();
				i++;
			}
		}
		if(rule!=null&&(param==2||param==-1))
			if(rule.rules!=null){
				R=rule.rules;
			}
	}
	/**
	 * Add new variable of the specified type and name
	 * @param n - name of the variable
	 * @param type - type of the variable
	 */
	public void addNewVar(String n,int type){
		DITVariable newVar=new DITVariable();
		newVar.Initialize(n, "0;1;", "0.05");
		switch(type){
			case 0:
				LIvars.add(newVar);
				break;
			case 1:
				LOvars.add(newVar);
				break;
			case 2:
			default: return;
		}
	};
	
	/**
	 * Modify variable of the specified type at the specified position in the list of variables
	 * @param n - name of the variable
	 * @param r - range of the variable
	 * @param d - discretisation of the variable
	 * @param type - type of the variable 
	 * @param pos - position of the variable 
	 */
	public void editVar(String n, String r, String d,int type,int pos){

		DITVariable Var=new DITVariable();
		switch(type){
		case 0:
			Var=LIvars.get(pos);
			Var.Initialize(n, r, d);
			break;
		case 1:
			Var=LOvars.get(pos);
			Var.Initialize(n, r, d);
			break;
		case 2:
		default: return;
		}
	};
	
	/**
	 * Do all the calculations with the library
	 * @return result of calculations
	 */
	public Double Calculate(Double[] IN){
		this.convertDatatoOutter(-1);
		Double res=null;
        TypeReducer tr = new TypeReducer();			   	
        Defuzzification defuzz = new Defuzzification();                                    
		try{ 
            //Fuzzifier of crisp input values
            Double[][] M = fuzz.GetMatrixM(IN, LI);
            //Inference
            im1.CalcRateRules(M, R, IN.length);
            Double[][][] Tact=im1.outTact();
            Double[][] Ract=im1.outRact();
            ArrayList<Double[][]> F = im2.CalcActMF(Ract, Tact, LO);
            Double[][] Y = im3.CalcResultMF(F);
            //Type-Reducer
            Double[] TR = tr.GetTypeReduceFS(Y);
            //Defuzzifier
            res = defuzz.GetValue(TR);
            this.result=res; 
		}catch (Exception ex){
			System.out.println(IN[0]+";\t" + IN[1] + ";\t" + ex.getMessage());
		}
		return res;
	};
	
	/**
	 * Check whether variable with the specified name already exists in the list of LI variables
	 * @param name - name of the variable
	 * @param index - index of the original variable
	 * @return true - if name doesn't exist, false - other way
	 */
	public boolean checkInputVariableName(String name,int index){
		if(this.LIvars==null) return true;
		int i=0;
		for(DITVariable var: this.LIvars){
			if(var.name.equals(name)&&i!=index) return false;
			i++;
		}
		return true;
	};
	
	/**
	 * Check whether variable with the specified name already exists in the list of LO variables
	 * @param name - name of the variable
	 * @param index - index of the original variable
	 * @return true - if name doesn't exist, false - other way
	 */
	public boolean checkOutputVariableName(String name,int index){
		if(this.LOvars==null) return true;
		int i=0;
		for(DITVariable var: this.LOvars){
			if(var.name.equals(name)&&i!=index) return false;
			i++;
		}
		return true;
	};
}
