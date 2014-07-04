package mlp.core;

public class ClientEntityStats {
	double maxLife, life, maxMagic, magic;
	
	public ClientEntityStats(double maxLife, double maxMagic, double life, double mana) {
		setNewMaxStats(maxLife, maxMagic);
		setNewCurrentStats(life, mana);
	}

	public void setNewCurrentStats(double life, double magic) {
		this.life = life;
		this.magic = magic;
	}
	
	public void setNewMaxStats(double life, double magic) {
		this.maxLife = life;
		this.maxMagic = magic;
	}
	
	public double getCurrentLife() { return life; }
	public double getCurrentMagic() { return magic; }
	
	public double getMaxLife() { return maxLife; }
	public double getMaxMagic() { return maxMagic; }
	
	public double getPercentageLife() { return life / maxLife; }
	public double getPercentageMagic() { return magic / maxMagic; }
}
