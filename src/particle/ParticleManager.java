package particle;

import java.util.ArrayList;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.Sprite;

import com.badlogic.gdx.math.Vector2;

import main.ResourcesManager;
import scene.BaseScene;
import ui.MyHUD;

public class ParticleManager implements IUpdateHandler {

	private static ParticleManager instance;
	private static boolean instanceCreated = false;
	
	private boolean rainEnabled = false;
	private ArrayList<RainDrop> rainDrops;
	private ArrayList<RainSplash> rainSplashes;
	private int rainSplashesPointer = 0;
	
	private boolean snowEnabled = false;
	private ArrayList<SnowParticle> snow;
	private boolean blizzardEnabled = false;
	private ArrayList<BlizzardParticle> blizzard;
	
	private boolean sparklesEnabled = false;
	private ArrayList<SparkleParticle> sparkles;
	private int sparklesPointer = 0;
	
	private boolean boomsEnabled = false;
	private ArrayList<BoomParticle> booms;
	private int boomsPointer = 0;
	private ArrayList<DebrisParticle> debris;
	private int debirsPointer = 0;
	
	private boolean shieldsEnabled = false;
	private boolean playerShieldEnabled = false;
	private ArrayList<ShieldParticle> playerShield;
	
	private boolean enemyShieldEnabled = false;
	private ArrayList<ShieldParticle> enemyShield;
	
	private boolean leafsEnabled = false;
	private ArrayList<LeafParticle> leafs;
	
	private boolean glyphPariclesEnabled = false;
	private int glyphPointer = 0;
	private ArrayList<GlyphParticle> glyphParticles;
	
	private boolean footprintsEnabled = false;
	private int footprintPointer = 0;
	private ArrayList<FootprintParticle> footprints;
	
	private boolean mistEnabled = false;
	private ArrayList<MistParticle> mist;
	
	private boolean mistAtMenuEnabled = false;
	private ArrayList<MistParticle> mistAtMenu;
	
	private boolean dirtEnabled = false;
	private ArrayList<DirtParticle> dirt;
	
	private boolean benchesEnabled = false;
	private ArrayList<Sprite> benches;
	private ArrayList<BenchParticle> benchParticles;
	private int benchParticlePointer = 0;
	
	private boolean smokeEnabled = false;
	private ArrayList<SmokeParticle> smoke;
	private int smokePointer = 0;
	
	private boolean mechDebrisParticleEnabled = false;
	private ArrayList<MechDebrisParticle> mechDebris;
	
	private BaseScene scene;
	private MyHUD hud;
	
	private boolean dispose = false;
	
	private ParticleManager(){
		rainDrops = new ArrayList<RainDrop>();
		rainSplashes = new ArrayList<RainSplash>();
		sparkles = new ArrayList<SparkleParticle>();
		enemyShield = new ArrayList<ShieldParticle>();
		playerShield = new ArrayList<ShieldParticle>();
		leafs = new ArrayList<LeafParticle>();
		glyphParticles = new ArrayList<GlyphParticle>();
		footprints = new ArrayList<FootprintParticle>();
		booms = new ArrayList<BoomParticle>();
		debris = new ArrayList<DebrisParticle>();
		mist = new ArrayList<MistParticle>();
		dirt = new ArrayList<DirtParticle>();
		benches = new ArrayList<Sprite>();
		benchParticles = new ArrayList<BenchParticle>();
		smoke = new ArrayList<SmokeParticle>();
		mechDebris = new ArrayList<MechDebrisParticle>();
		snow = new ArrayList<SnowParticle>();
		blizzard = new ArrayList<BlizzardParticle>();
		mistAtMenu = new ArrayList<MistParticle>();
	}
	
	public void setScene(BaseScene scene){
		this.scene = scene;
	}
	
	public void setHUD(MyHUD hud){
		this.hud = hud;
	}
	
	public static ParticleManager getInstance(){
		if(instanceCreated){
			return instance;
		}else{
			instanceCreated = true;
			return instance = new ParticleManager();
		}
	}
	
	public void dispose(){
		dispose = true;
	}
	
	@Override
	public void onUpdate(float pSecondsElapsed) {
		if(rainEnabled){
			for(RainDrop rd : rainDrops){
				rd.update();
				if(rd.createRainSplash){
					rd.createRainSplash = false;
					float randX = (float) (ResourcesManager.getInstance().rand.nextDouble()*900 - 450);
					float randY = (float) (ResourcesManager.getInstance().rand.nextDouble()*500 - 250);
					float randAngle = ResourcesManager.getInstance().rand.nextFloat()*360;
					for(int i=0; i<3; i++){
						Sprite rs = rainSplashes.get(rainSplashesPointer);
						rainSplashesPointer++;
						if(rainSplashesPointer >= rainSplashes.size()){
							rainSplashesPointer = 0;
						}
						rs.setVisible(true);
						rs.setRotation((float)randAngle + 120*i);
						rs.setPosition(hud.getCamera().getCenterX() + randX, hud.getCamera().getCenterY() + randY);
					}
				}
			}
			for(RainSplash rs : rainSplashes){
				rs.update();
			}
		}
		if(snowEnabled){
			for(SnowParticle sp : snow){
				sp.update();
			}
		}
		if(sparklesEnabled){
			for(SparkleParticle sp : sparkles){
				sp.update();
			}
		}
		if(boomsEnabled){
			for(BoomParticle bp : booms){
				bp.update();
			}
			for(DebrisParticle dp : debris){
				dp.update();
			}
		}
		if(shieldsEnabled){
			if(playerShieldEnabled){
				for(ShieldParticle sh : playerShield){
					sh.update();
				}
			}
			if(enemyShieldEnabled){
				for(ShieldParticle sh : enemyShield){
					sh.update();
				}
			}
		}
		if(leafsEnabled){
			for(LeafParticle leaf : leafs){
				leaf.update();
			}
		}
		if(glyphPariclesEnabled){
			for(GlyphParticle gp : glyphParticles){
				gp.update();
			}
		}
		if(footprintsEnabled){
			for(FootprintParticle fp : footprints){
				fp.update();
			}
		}
		if(blizzardEnabled){
			for(BlizzardParticle bp : blizzard){
				bp.update();
			}
		}
		if(mistEnabled){
			for(MistParticle mp : mist){
				mp.update();
			}
		}
		if(dirtEnabled){
			for(DirtParticle dp : dirt){
				dp.update();
			}
		}
		if(smokeEnabled){
			for(SmokeParticle sp : smoke){
				sp.update();
			}
		}
		if(mechDebrisParticleEnabled){
			for(MechDebrisParticle mdp : mechDebris){
				mdp.update();
			}
		}
		if(benchesEnabled){
			for(BenchParticle bp : benchParticles){
				bp.update();
			}
			boolean destroy = true;
			for(Sprite b : benches){
				if(b.isVisible()){
					destroy = false;
				}
			}
			for(BenchParticle bp : benchParticles){
				if(!bp.isDestroyed()){
					destroy = false;
				}
			}
			if(destroy){
				for(Sprite b : benches){
					b.detachSelf();
				}
				benches.clear();
				for(BenchParticle bp : benchParticles){
					bp.destroy();
				}
				benchParticles.clear();
				benchesEnabled = false;
			}
		}
		
		if(dispose){
			rainEnabled = false;
			enemyShieldEnabled = false;
			playerShieldEnabled = false;
			glyphPariclesEnabled = false;
			shieldsEnabled = false;
			sparklesEnabled = false;
			leafsEnabled = false;
			dispose = false;
			snowEnabled = false;
			footprintsEnabled = false;
			boomsEnabled = false;
			mistEnabled = false;
			dirtEnabled = false;
			benchesEnabled = false;
			smokeEnabled = false;
			mechDebrisParticleEnabled = false;
			snowEnabled = false;
			blizzardEnabled = false;
			mistAtMenuEnabled = false;
			for(RainDrop r : rainDrops){
				r.detachSelf();
			}
			rainDrops.clear();
			for(Sprite rs : rainSplashes){
				rs.detachSelf();
			}
			rainSplashes.clear();
			for(Sprite sp : sparkles){
				sp.detachSelf();
			}
			sparkles.clear();
			for(Sprite sp : playerShield){
				sp.detachSelf();
			}
			playerShield.clear();
			for(Sprite sp : enemyShield){
				sp.detachSelf();
			}
			enemyShield.clear();
			for(LeafParticle lp : leafs){
				lp.detachSelf();
			}
			leafs.clear();
			for(GlyphParticle gp : glyphParticles){
				gp.detachSelf();
			}
			glyphParticles.clear();
			for(FootprintParticle fp : footprints){
				fp.detachSelf();
			}
			footprints.clear();
			for(SnowParticle sp : snow){
				sp.detachSelf();
			}
			snow.clear();
			for(BlizzardParticle bp : blizzard){
				bp.detachSelf();
			}
			blizzard.clear();
			for(BoomParticle bp : booms){
				bp.detachSelf();
			}
			booms.clear();
			for(DebrisParticle dp : debris){
				dp.detachSelf();
			}
			debris.clear();
			for(MistParticle mp : mist){
				mp.detachSelf();
			}
			mist.clear();
			for(DirtParticle dp : dirt){
				dp.detachSelf();
			}
			dirt.clear();
			for(Sprite b : benches){
				b.detachSelf();
			}
			benches.clear();
			for(BenchParticle bp : benchParticles){
				bp.destroy();
			}
			benchParticles.clear();
			for(SmokeParticle sp : smoke){
				sp.detachSelf();
			}
			smoke.clear();
			for(MechDebrisParticle mdp : mechDebris){
				mdp.detachSelf();
			}
			mechDebris.clear();
			for(MistParticle mp : mistAtMenu){
				mp.detachSelf();
			}
			mistAtMenu.clear();
		}
	}
	
	public void updateMistAtMenu(){
		if(mistAtMenuEnabled){
			for(MistParticle mp : mistAtMenu){
				mp.update();
			}
		}
	}
	
	public void sparkle(float x, float y){
		if(!sparklesEnabled){
			return;
		}
		for(int i=0; i<3; i++){
			SparkleParticle sp = sparkles.get(sparklesPointer);
			sparklesPointer++;
			if(sparklesPointer >= sparkles.size()){
				sparklesPointer = 0;
			}
			float randAngle = ResourcesManager.getInstance().rand.nextFloat()*360;
			sp.setRotation(randAngle);
			sp.setVisible(true);
			sp.setPosition(x, y);
			sp.setAlpha(0.8f);
		}
	}
	
	public void boom(float x, float y){
		if(!boomsEnabled){
			return;
		}
		BoomParticle bp = booms.get(boomsPointer);
		boomsPointer++;
		if(boomsPointer >= booms.size()){
			boomsPointer = 0;
		}
		bp.fire(x, y);
		for(int i=0; i<10; i++){
			DebrisParticle dp = debris.get(debirsPointer);
			debirsPointer++;
			if(debirsPointer >= debris.size()){
				debirsPointer = 0;
			}
			dp.fire(x, y);
		}
	}
	
	public void enableBooms(){
		boomsEnabled = true;
		booms = new ArrayList<BoomParticle>();
		debris = new ArrayList<DebrisParticle>();
		for(int i=0; i<5; i++){
			BoomParticle bp = new BoomParticle();
			booms.add(bp);
			scene.attachChildAtForeground(bp);
		}
		for(int i=0; i<20; i++){
			DebrisParticle dp = new DebrisParticle();
			debris.add(dp);
			scene.attachChildAtForeground(dp);
		}
	}
	
	public void enableDirtParticles(){
		dirtEnabled = true;
		dirt = new ArrayList<DirtParticle>();
		for(int i=0; i<25; i++){
			DirtParticle dp = new DirtParticle();
			dirt.add(dp);
			scene.attachChildAtForeground(dp);
		}
	}
	
	public void fireDirt(float x, float y){
		if(dirtEnabled){
			for(DirtParticle dp : dirt){
				dp.fire(x, y);
			}
		}
	}
	
	public void startPlayerArmor(){
		playerShieldEnabled = true;
		for(ShieldParticle sp : playerShield){
			sp.start();
		}
	}
	
	public void stopPlayerArmor(){
		playerShieldEnabled = false;
		for(ShieldParticle sp : playerShield){
			sp.setVisible(false);
		}
	}
	
	public void startEnemyArmor(){
		enemyShieldEnabled = true;
		for(ShieldParticle sp : enemyShield){
			sp.start();
		}
	}
	
	public void stopEnemyArmor(){
		enemyShieldEnabled = false;
		for(ShieldParticle sp : enemyShield){
			sp.setVisible(false);
		}
	}
	
	public void startSnow(){
		snowEnabled = true;
		snow = new ArrayList<SnowParticle>();
		for(int i = 0; i < 16; i++){
			double randAngle = ResourcesManager.getInstance().rand.nextDouble()*360;
			double randDist = ResourcesManager.getInstance().rand.nextDouble()*600;
			double dx = randDist*Math.cos(Math.toRadians(randAngle));
			double dy = randDist*Math.sin(Math.toRadians(randAngle));
			SnowParticle sp = new SnowParticle((float)dx, (float)dy);
			hud.attachChild(sp);
			snow.add(sp);
			sp.setScale((float)randDist/400f);
			sp.setRotation((float)-randAngle);
			sp.setAlpha(0.2f);
		}
	}
	
	public void startMist(){
		mistEnabled = true;
		mist = new ArrayList<MistParticle>();
		for(int i=0; i<5; i++){
			MistParticle mp = new MistParticle();
			mist.add(mp);
			scene.attachChildAtForeground(mp);
		}
	}
	
	public void startMistAtMenu(){
		mistAtMenuEnabled = true;
		mistAtMenu = new ArrayList<MistParticle>();
		for(int i=0; i<5; i++){
			MistParticle mp = new MistParticle();
			mistAtMenu.add(mp);
			scene.attachChildAtBackground(mp);
			mp.startPoint = new Vector2(1024, 552);
			mp.bounds = new Vector2(0, 0);
			mp.setAlpha(0.1f);
			mp.maxAlpha = 0.1f;
			mp.randRange.x = 500;
			mp.randRange.y = 400;
			mp.direction.x = -(ResourcesManager.getInstance().rand.nextFloat() + 0.3f);
			mp.direction.y = mp.direction.x + 0.2f;
		}
		mistAtMenu.get(0).registerUpdateHandler(new IUpdateHandler() {
			@Override
			public void reset() {
			}
			@Override
			public void onUpdate(float pSecondsElapsed) {
				updateMistAtMenu();
				
			}
		});
	}
	
	public void changeMistAtMenuScreen(final BaseScene newScene){
		if(!mistAtMenuEnabled){
			return;
		}
		ResourcesManager.getInstance().engine.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				for(MistParticle mp : mistAtMenu){
					scene.detachChild(mp);
					mp.detachSelf();
					newScene.attachChildAtBackground(mp);
					setScene(newScene);
					
				}
			}
		});
		
		
		
	}
	
	public boolean mistAtMenuEnabled(){
		return mistAtMenuEnabled;
	}
	
	public void disableMistAtMenu(){
		mistAtMenuEnabled = false;
		for(MistParticle mp : mistAtMenu){
			mp.detachSelf();
		}
		mistAtMenu.clear();
	}
	
	public void startRain(){
		rainEnabled = true;
		dispose = false;
		rainDrops = new ArrayList<RainDrop>();
		rainSplashes = new ArrayList<RainSplash>();
		for(int i=0; i<60; i++){
			int kind = ResourcesManager.getInstance().rand.nextInt(3);
			double randAngle = ResourcesManager.getInstance().rand.nextDouble()*360;
			double randDist = ResourcesManager.getInstance().rand.nextDouble()*600;
			double dx = randDist*Math.cos(Math.toRadians(randAngle));
			double dy = randDist*Math.sin(Math.toRadians(randAngle));
			RainDrop rainDrop = new RainDrop((float)dx + 1024/2f, (float)dy + 552/2f);
			if(kind == 0){
				rainDrop.setFlippedHorizontal(true);
			}
			if(kind == 1){
				rainDrop.setFlippedVertical(true);
			}
			if(kind == 2){
				rainDrop.setFlippedHorizontal(true);
				rainDrop.setFlippedVertical(true);
			}
			rainDrop.setScaleX((float)randDist/400f);
			rainDrop.setScaleY(0.5f);
			rainDrop.setRotation((float)-randAngle);
			hud.attachChild(rainDrop);
			rainDrops.add(rainDrop);
			rainDrop.setAlpha(0.2f);
		}
		for(int i=0; i<30; i++){
			RainSplash rainSplash = new RainSplash();
			scene.attachChild(rainSplash);
			rainSplashes.add(rainSplash);
		}
	}
	
	public void enableShields(IEntity player, IEntity enemy){
		shieldsEnabled = true;
		playerShield = new ArrayList<ShieldParticle>();
		enemyShield = new ArrayList<ShieldParticle>();
		for(int i=0; i<4; i++){
			ShieldParticle shPlayer = new ShieldParticle(player);
			playerShield.add(shPlayer);
			scene.attachChild(shPlayer);
			
			ShieldParticle shEnemy = new ShieldParticle(enemy);
			enemyShield.add(shEnemy);
			scene.attachChild(shEnemy);
		}
	}
	
	public void addSparkles(){
		sparklesEnabled = true;
		dispose = false;
		sparkles = new ArrayList<SparkleParticle>();
		for(int i=0; i<10; i++){
			SparkleParticle sp = new SparkleParticle();
			scene.attachChild(sp);
			sparkles.add(sp);
		}
	}
	
	
	
	public void enableGlyphParticles(){
		glyphPariclesEnabled = true;
		glyphParticles = new ArrayList<GlyphParticle>();
		for(int i=0; i<18; i++){
			GlyphParticle gp = new GlyphParticle();
			glyphParticles.add(gp);
			scene.attachChild(gp);
		}
	}
	
	public void enableLeafs(){
		leafsEnabled = true;
		leafs = new ArrayList<LeafParticle>();
		for(int i=0; i<40; i++){
			LeafParticle lp = new LeafParticle(ResourcesManager.getInstance().rand.nextFloat()*600 - 300,
					ResourcesManager.getInstance().rand.nextFloat()*800 - 400);
			leafs.add(lp);
			scene.attachChildAtBackground2(lp);
		}
	}
	
	public void enableBlizzard(){
		blizzardEnabled = true;
		blizzard = new ArrayList<BlizzardParticle>();
		for(int i=0; i<6; i++){
			BlizzardParticle bp = new BlizzardParticle(ResourcesManager.getInstance().rand.nextFloat()*600 - 300,
					ResourcesManager.getInstance().rand.nextFloat()*800 - 400);
			blizzard.add(bp);
			scene.attachChildAtBackground2(bp);
		}
	}
	
	public void enableFootprints(){
		footprintsEnabled = true;
		footprints = new ArrayList<FootprintParticle>();
		for(int i = 0; i < 32; i++){
			FootprintParticle fp = new FootprintParticle();
			scene.attachChildAtBackground2(fp);
			footprints.add(fp);
		}
	}
	
	public void shootGlyphs(float x, float y){
		for(int i=0; i<3; i++){
			float randAngle = ResourcesManager.getInstance().rand.nextFloat();
			float randD = ResourcesManager.getInstance().rand.nextFloat()*54;
			float dx = (float) (randD*Math.cos(randAngle));
			float dy = (float) (randD*Math.sin(randAngle));
			GlyphParticle gp = glyphParticles.get(glyphPointer);
			glyphPointer++;
			if(glyphPointer >= glyphParticles.size()){
				glyphPointer = 0;
			}
			gp.setPosition(x + dx, y+dy);
			gp.fire();
		}
	}
	
	public void enableBenches(final IEntity player, final IEntity enemy){
		benchesEnabled = true;
		benches = new ArrayList<Sprite>();
		benchParticles = new ArrayList<BenchParticle>();
		for(int i=0; i<10; i++){
			for(int j=0; j<4; j++){
				BenchParticle bp = new BenchParticle();
				benchParticles.add(bp);
				scene.attachChildAtBackground2(bp);
			}
			final Sprite bench = new Sprite(ResourcesManager.getInstance().rand.nextFloat()*300 - 150 - 450,
					ResourcesManager.getInstance().rand.nextFloat()*400 - 200 + 200,
					ResourcesManager.getInstance().bench, ResourcesManager.getInstance().vbom);
			bench.setRotation(ResourcesManager.getInstance().rand.nextFloat()*360);
			scene.attachChildAtBackground2(bench);
			benches.add(bench);
			bench.registerUpdateHandler(new IUpdateHandler() {
				@Override
				public void reset() {}
				@Override
				public void onUpdate(float pSecondsElapsed) {
					if(bench.isVisible()){
						if(bench.collidesWith(player) || bench.collidesWith(enemy)){
							bench.setVisible(false);
							fireBenchParticles(bench.getX(), bench.getY());
						}
					}
				}
			});
		}
	}
	
	public void fireBenchParticles(float x, float y){
		for(int i=0; i<4; i++){
			BenchParticle bp = benchParticles.get(benchParticlePointer);
			benchParticlePointer++;
			if(benchParticlePointer >= benchParticles.size()){
				benchParticlePointer = 0;
			}
			bp.fire(x, y);
		}
	}
	
	public void addFootPrint(float x, float y){
		if(footprintsEnabled){
			FootprintParticle fp = footprints.get(footprintPointer);
			footprintPointer++;
			if(footprintPointer >= footprints.size()){
				footprintPointer = 0;
			}
			fp.fire(x, y);
		}
	}
	
	public void enableSmoke(){
		smokeEnabled = true;
		smoke = new ArrayList<SmokeParticle>();
		for(int i=0; i<20; i++){
			SmokeParticle sp = new SmokeParticle();
			scene.attachChildAtForeground(sp);
			smoke.add(sp);
		}
	}
	
	public void enableMechDebris(int y){
		mechDebrisParticleEnabled = true;
		mechDebris = new ArrayList<MechDebrisParticle>();
		for(int i=0; i<4; i++){
			MechDebrisParticle mdp = new MechDebrisParticle(i, 0);
			scene.attachChildAtBackground2(mdp);
			mechDebris.add(mdp);
		}
		for(int i=0; i<4; i++){
			MechDebrisParticle mdp = new MechDebrisParticle(i, y);
			scene.attachChildAtBackground2(mdp);
			mechDebris.add(mdp);
		}
	}
	
	public void firePlayerMechDebris(float x, float y){
		if(mechDebrisParticleEnabled){
			for(int i=0; i<4; i++){
				MechDebrisParticle mdp = mechDebris.get(i);
				mdp.fire(x, y);
			}
		}
	}
	
	public void fireEnemyMechDebris(float x, float y){
		if(mechDebrisParticleEnabled){
			for(int i=4; i<8; i++){
				MechDebrisParticle mdp = mechDebris.get(i);
				mdp.fire(x, y);
			}
		}
	}
	
	public void fireSmoke(float x, float y, float angle){
		if(!smokeEnabled){
			return;
		}
		SmokeParticle sp = smoke.get(smokePointer);
		smokePointer++;
		if(smokePointer >= smoke.size()){
			smokePointer = 0;
		}
		sp.fire(x, y, angle);
	}
	
	public void fireSmoke2(float x, float y){
		if(!smokeEnabled){
			return;
		}
		SmokeParticle sp = smoke.get(smokePointer);
		smokePointer++;
		if(smokePointer >= smoke.size()){
			smokePointer = 0;
		}
		sp.fire2(x, y);
	}

	@Override
	public void reset() {
		
	}
	
}
