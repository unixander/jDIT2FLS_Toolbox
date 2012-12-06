package pimf;

import standartmf.StandartMF;

public class PiMF extends StandartMF {

	public PiMF() {
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
		Double a = paramsMF[0], b = paramsMF[1], c = paramsMF[2], d = paramsMF[3];

		for (int i = 0; i < n; i++) {
			if (x[i] < a) {
				func[i] = new Double(0);
			} else if (x[i] < ((a + b) / 2.0)) {
				func[i] = 2.0 * Math.pow((x[i] - a) / (b - a), 2);
			} else if (x[i] < b) {
				func[i] = 1 - 2.0 * Math.pow((x[i] - b) / (b - a), 2);
			} else if (x[i] < c) {
				func[i] = new Double(1);
			} else if (x[i] < (c + d) / 2.0) {
				func[i] = 1 - 2.0 * Math.pow((x[i] - c) / (d - c), 2);
			} else if (x[i] < d) {
				func[i] = 2.0 * Math.pow((x[i] - d) / (d - c), 2);
			} else
				func[i] = new Double(0);
		}
		return func;
	}

	@Override
	public Double[] GetInitialParams(Double lp, Double rp) {
		// TODO Auto-generated method stub
		return null;
	}

}
