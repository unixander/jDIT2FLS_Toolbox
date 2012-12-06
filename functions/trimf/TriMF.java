package trimf;

import standartmf.StandartMF;

public class TriMF extends StandartMF {

	public TriMF()
	{
		this.countParams = 3;
		this.paramsMF = new Double[this.countParams];
	}
	
	@Override
	public Double[] GetDiscreteFunction(Double[] x, Double[] paramsMF) {
		if (this.paramsMF.length == paramsMF.length)
			this.paramsMF = paramsMF;
			else
			return null;

		Double a = this.paramsMF[0],
		b = this.paramsMF[1],
		c = this.paramsMF[2];
		int n = x.length;
		Double[] func = new Double[n];

		for (int i = 0; i < n; i++){
			if (x[i] <= a)
				func[i] = new Double(0);
			else if (x[i] > a && x[i] <= b)
				func[i] = (x[i] - a) / (b - a);
			else if (x[i] > b && x[i] <= c)
				func[i] = (c - x[i]) / (c - b);
			else if (x[i] >= c)
				func[i] = new Double(0);
		}
		return func;
	}

	@Override
	public Double[] GetInitialParams(Double lp, Double rp) {
		return new Double[] { lp, (rp - lp) / 2.0, rp }; 
	}

}
