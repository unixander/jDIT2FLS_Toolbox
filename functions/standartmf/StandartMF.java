package standartmf;

public abstract class StandartMF {
	public int countParams;
	public Double [] paramsMF;

	public abstract Double[] GetDiscreteFunction(Double[] x, Double[] paramsMF);
	public abstract Double[] GetInitialParams(Double lp, Double rp);
}
