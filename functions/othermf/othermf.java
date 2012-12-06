package othermf;

import standartmf.StandartMF;

public class othermf extends StandartMF {

	@Override
	public Double[] GetDiscreteFunction(Double[] x, Double[] paramsMF) {
		Double[] func = new Double[x.length];
		for(int i=0;i<x.length;i++){
			func[i]=new Double(paramsMF[0]);
		}
		return func;
	}

	@Override
	public Double[] GetInitialParams(Double lp, Double rp) {
		// TODO Auto-generated method stub
		return null;
	}

}
