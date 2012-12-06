package psigmf;

import standartmf.StandartMF;

public class PsigMF extends StandartMF {

	public PsigMF() {
		this.countParams = 4;
		this.paramsMF = new Double[this.countParams];
	}

	@Override
	public Double[] GetDiscreteFunction(Double[] x, Double[] paramsMF) {
		if (this.paramsMF.length == paramsMF.length)
			this.paramsMF = paramsMF;
		else
			return null;

		int n = x.length;
		Double[] func = new Double[n];
		Double a1 = paramsMF[0], c1 = paramsMF[1], a2 = paramsMF[2], c2 = paramsMF[3];

		for (int i = 0; i < n; i++) {
			func[i] = function(x[i], a1, c1) * function(x[i], a2, c2);
		}
		return func;
	}

	private Double function(Double x, Double a, Double c) {
		Double res = 0.0;
		res = 1.0 / (1 + Math.pow(Math.E, (-a) * (x - c)));
		return res;
	}

	@Override
	public Double[] GetInitialParams(Double lp, Double rp) {
		// TODO Auto-generated method stub
		return null;
	}

}
