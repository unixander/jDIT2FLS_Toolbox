package zmf;

import standartmf.StandartMF;

public class ZMF extends StandartMF {

	public ZMF() {
		this.countParams = 2;
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
		Double a = paramsMF[0], b = paramsMF[1];
		
		for(int i=0;i<n;i++){
			if(x[i]<a){
				func[i]=1.0;
			} else if(x[i]<(a+b)/2.0){
				func[i]=1-2.0*Math.pow((x[i]-a)/(b-a), 2);
			} else if(x[i]<b){
				func[i]=2.0*Math.pow((x[i]-b)/(b-a), 2);
			} else 
				func[i]=0.0;
		}
		return func;
	}

	@Override
	public Double[] GetInitialParams(Double lp, Double rp) {
		// TODO Auto-generated method stub
		return null;
	}

}
