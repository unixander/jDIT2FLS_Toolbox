package gaussmf;

import standartmf.StandartMF;

public class GaussMF extends StandartMF {

	public GaussMF()
	{
		this.countParams = 2;
		this.paramsMF = new Double[this.countParams];
	}
	
	@Override
	public Double[] GetDiscreteFunction(Double[] x, Double[] paramsMF) {
		if (this.paramsMF.length == paramsMF.length)
			this.paramsMF = paramsMF;
			else
			return null;

		Double  c = this.paramsMF[0],
				b = this.paramsMF[1];

		int n = x.length;
		Double[] func = new Double[n];

		for (int i = 0; i < n; i++){
			func[i]=Math.exp(-(Math.pow((x[i]-b), 2)/(2*c*c)));
		}
		return func;
	}

	@Override
	public Double[] GetInitialParams(Double lp, Double rp) {
		return new Double[] { lp, (rp - lp) / 2.0, rp }; 
	}

}
