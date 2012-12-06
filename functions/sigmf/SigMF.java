package sigmf;

import standartmf.StandartMF;

public class SigMF extends StandartMF {

	public SigMF() {
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
		Double a = paramsMF[0], c = paramsMF[1];

		for (int i = 0; i < n; i++) {
			func[i] = 1.0 / (1 + Math.pow(Math.E, (-a) * (x[i] - c)));
		}
		return func;
	}

	@Override
	public Double[] GetInitialParams(Double lp, Double rp) {
		// TODO Auto-generated method stub
		return null;
	}

}
