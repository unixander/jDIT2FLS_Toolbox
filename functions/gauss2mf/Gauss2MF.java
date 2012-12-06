package gauss2mf;

import standartmf.StandartMF;

public class Gauss2MF extends StandartMF {

	public Gauss2MF()
	{
		this.countParams = 4;
		this.paramsMF = new Double[this.countParams];
	}
	
	@Override
	public Double[] GetDiscreteFunction(Double[] x, Double[] paramsMF) {
		if (this.paramsMF.length == paramsMF.length)
			this.paramsMF = paramsMF;
			else
			return null;
		
		Double  a1=this.paramsMF[0],
				c1=this.paramsMF[1],
				a2=this.paramsMF[2],
				c2=this.paramsMF[3];
		
		boolean subnormal=(c1>c2);
		int n=x.length;
		Double[] func = new Double[n];
		for (int i = 0; i < n; i++){
		 if(subnormal){
			 if(x[i]<c2){
				 func[i]=Math.exp(Math.pow((x[i]-c1), 2)/(-2*Math.pow(a1, 2)));
			 } else if(x[i]<=c1||x[i].equals(c2)) {
				 func[i]=Math.exp(Math.pow((x[i]-c1), 2)/(-2*Math.pow(a1, 2)))*Math.exp(Math.pow((x[i]-c2), 2)/(-2*Math.pow(a2, 2)));
			 } else if(x[i]>c1){
				 func[i]=Math.exp(Math.pow((x[i]-c2), 2)/(-2*Math.pow(a2, 2)));
			 }
		 } else {
			 if(x[i]<c1){
				 func[i]=Math.exp(Math.pow((x[i]-c1), 2)/(-2*Math.pow(a1, 2)));
			 } else if(x[i]<=c2||x[i].equals(c1)) {
				 func[i]=new Double(1);
			 } else if(x[i]>c2){
				 func[i]=Math.exp(Math.pow((x[i]-c2), 2)/(-2*Math.pow(a2, 2)));
			 }
		 }
		};
		return func;
	}

	@Override
	public Double[] GetInitialParams(Double lp, Double rp) {
		return new Double[] { lp, (rp - lp) / 2.0, rp };
	}

}
