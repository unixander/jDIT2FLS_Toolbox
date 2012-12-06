package gbellmf;

import standartmf.StandartMF;

public class GbellMF extends StandartMF {

	public GbellMF() {
		this.countParams = 3;
		this.paramsMF = new Double[this.countParams];
	}

	@Override
	public Double[] GetDiscreteFunction(Double[] x, Double[] paramsMF) {
		if (this.paramsMF.length == paramsMF.length)
			this.paramsMF = paramsMF;
			else
			return null;
		
		int n=x.length;
		Double[] func = new Double[n];
		Double a=paramsMF[0],
			   b=paramsMF[1],
			   c=paramsMF[2];
		
		for(int i=0;i<n;i++){
			func[i]=1.0/(1+Math.pow(Math.abs((x[i]-c)/a), 2*b));
		}
		return func;
	}

	@Override
	public Double[] GetInitialParams(Double lp, Double rp) {
		// TODO Auto-generated method stub
		return null;
	}

}
