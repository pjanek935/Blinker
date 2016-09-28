package world;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.input.touch.TouchEvent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import main.BaseScene;
import main.MyCamera;
import main.ResourcesManager;
import particle.ParticleManager;
import player.Box;
import player.Drone;
import player.Enemy;
import player.Fist;
import player.Kid;
import player.Mech;
import player.Mole;
import player.Phantom;
import player.Player;
import player.Swarm;
import player.Witch;
import projectiles.BouncingBullet;
import projectiles.Bullet;
import projectiles.DestroyableBouncingBullet;
import projectiles.EnemyProjectile;
import projectiles.FireProjectile;
import projectiles.HealingProjectile;
import projectiles.PhantomProjectile;
import projectiles.Projectile;
import projectiles.SuperBullet;
import ui.MyHUD;

public class World {
	
	public enum GameState{
	NONE, LOST, WON	
	}
	
	//public static float timeFactor;
	public static boolean screenPressed = false;
	public static boolean readyToStart = false;
	public static StageType stageType;
	public static GameState gameState = GameState.NONE;
	public boolean lost = false;
	
	public Stage stage;
	public PhysicsWorld physicsWorld;
	
	public Player player;
	public Enemy enemy;
	
	public MyHUD hud;
	public MyCamera camera;
	
	public World(BaseScene scene){
		camera = ResourcesManager.getInstance().camera;
		hud =  new MyHUD(scene, camera);
		ParticleManager.getInstance().setHUD(hud);
		physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, 0), true);
		scene.registerUpdateHandler(physicsWorld);
		player = new Player(physicsWorld, scene, 0, 0, hud);
		switch(stageType){
    	case STAGE_TUTORIAL:
    		enemy = new Box(0, 200, physicsWorld, scene, player);
    		stage = new StageTutorial(scene, physicsWorld, player.getBodySprite(), enemy.getBodySprite());
    		break;
    	case STAGE1:
    		enemy = new Mole(0, 100, physicsWorld, scene, player);
    		stage = new Stage1(scene, physicsWorld, player.getBodySprite(), enemy.getBodySprite());
    		break;
    	case STAGE2:
    		enemy = new Fist(-100, 200, physicsWorld, scene, player);
    		stage = new Stage2(scene, physicsWorld, player.getBodySprite(), enemy.getBodySprite());
    		break;
    	case STAGE3:
    		enemy = new Witch(-100, -200, physicsWorld, scene, player);
    		stage = new Stage3(scene, physicsWorld, player.getBodySprite(), enemy.getBodySprite());
    		break;
    	case STAGE4:
    		enemy = new Swarm(-100, 200, physicsWorld, scene, player);
    		stage = new Stage4(scene, physicsWorld, player.getBodySprite(), enemy.getBodySprite());
    		break;
    	case STAGE5:
    		enemy = new Phantom(-100, 200, physicsWorld, scene, player);
    		stage = new Stage5(scene, physicsWorld, player.getBodySprite(), enemy.getBodySprite());
    		break;
    	case STAGE_EPILOGUE:
    		enemy = new Kid(-100, 200, physicsWorld, scene, player);
    		stage = new StageEpilogue(scene, physicsWorld, player.getBodySprite(), enemy.getBodySprite());
    		break;
    	default:
    		break;
    	}
		player.setEnemy(enemy);
		stage.loadStage();
		physicsWorld.setContactListener(createContactListener());
		ParticleManager.getInstance().setScene(scene);
		scene.registerUpdateHandler(ParticleManager.getInstance());
		
		
		hud.setPlayer(player);
		hud.setEnemy(enemy);
		
		
		camera.setHUD(hud);
		camera.setChaseEntity(player.getBodySprite());
		camera.setEnemy(enemy.getBodySprite());
		camera.follow = true;
		
		player.setHpBar(hud.getPlayerHpBar());
		player.setBlinkBar(hud.getBlinkBar());
		enemy.setHpBar(hud.getEnemyHpBar());
		ParticleManager.getInstance().addSparkles();
		ParticleManager.getInstance().enableShields(player.getBodySprite(), enemy.getBodySprite());
		ParticleManager.getInstance().enableBooms();
		ParticleManager.getInstance().enableSmoke();
		
		if(stage instanceof Stage5){
			Stage5 stage5 = (Stage5)stage;
			stage5.setUpdateHandler(player.getBodySprite(), enemy.getBodySprite());
			ParticleManager.getInstance().enableBenches(player.getBodySprite(), enemy.getBodySprite());
			ParticleManager.getInstance().enableMechDebris(5);
		}else if(stage instanceof Stage4){
			ParticleManager.getInstance().enableMechDebris(4);
		}else if(stage instanceof Stage3){
			ParticleManager.getInstance().enableMechDebris(3);
		}else if(stage instanceof Stage2){
			ParticleManager.getInstance().enableMechDebris(2);
		}else if(stage instanceof Stage1){
			ParticleManager.getInstance().enableMechDebris(1);
		}
		
		final World world = this;
		scene.registerUpdateHandler(new IUpdateHandler() {
			@Override
			public void reset() {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onUpdate(float pSecondsElapsed) {
				if(gameState == GameState.WON){
					readyToStart = false;
					player.setMaxHP(100);
					hud.setControlsVisible(false);
					stage.playerWon();
					gameState = GameState.NONE;
				}
				if(gameState == GameState.LOST && (!lost)){
					gameState = GameState.NONE;
					readyToStart = false;
					hud.setControlsVisible(false);
					//stage.playerLost();
					hud.holdBlackScreen = true;
					hud.blackScreen.setAlpha(0);
					hud.blackScreen.setVisible(true);
					lost = true;
					player.destroyMech();
					ResourcesManager.getInstance().vibrator.vibrate(1000);
				}
				if(lost){
					hud.blackScreen.setAlpha(hud.blackScreen.getAlpha() + 0.004f);
					//Log.d("mine", "Black screen alpha: " + hud.getAlpha());
					if(hud.blackScreen.getAlpha() > 0.3){
						if(world.screenPressed){
							hud.exit();
						}
					}
					
					if(hud.blackScreen.getAlpha() >= 0.99f){
						hud.exit();
					}
				}else{
					stage.stageLogic(world);
				}
				
			}
		});
	}
	
	public void dispose(){
		camera.follow = false;
		camera.setCenter(512, 276);
		camera.setHUD(null);
		camera.dispose();
		readyToStart = false;
		lost = false;
		gameState = GameState.NONE;
		player.setMaxHP(100);
		ParticleManager.getInstance().dispose();
	}
	
	public void input(Scene pScene, TouchEvent pSceneTouchEvent){
		
	}
	
	//
	//Collision handling
	//
	private ContactListener createContactListener(){
		
		ContactListener contactListener = new ContactListener(){

			@Override
			public void beginContact(Contact contact) {
				
				Fixture fixA = contact.getFixtureA();
				Fixture fixB = contact.getFixtureB();
				
				if(fixA.getBody().getUserData() instanceof PhantomProjectile ||
						fixB.getBody().getUserData() instanceof PhantomProjectile){
					return;
				}
				
				if((fixA.getBody().getUserData() instanceof Bullet && fixB.getBody().getUserData() instanceof FireProjectile) ||
						(fixB.getBody().getUserData() instanceof Bullet && fixA.getBody().getUserData() instanceof FireProjectile)){
					return;
				}
				
				if((fixA.getBody().getUserData() instanceof HealingProjectile &&
						fixB.getBody().getUserData() instanceof EnemyProjectile) ||
						(fixB.getBody().getUserData() instanceof HealingProjectile &&
								fixA.getBody().getUserData() instanceof EnemyProjectile)){
					return;
				}
				
				if(fixA.getBody().getUserData() instanceof Drone &&
						fixB.getBody().getUserData() instanceof HealingProjectile){
					return;
				}
				
				if(fixB.getBody().getUserData() instanceof Drone &&
						fixA.getBody().getUserData() instanceof HealingProjectile){
					return;
				}
				
				if(fixA.getBody().getUserData() instanceof Projectile && fixB.getBody().getUserData() instanceof Projectile){
					if(fixA.getBody().getUserData().getClass().equals(fixB.getBody().getUserData().getClass())){
						return;
					}
				}
				
				if(fixA.getBody().getUserData() instanceof EnemyProjectile && fixB.getBody().getUserData() instanceof EnemyProjectile){
					return;
				}
				
				if( (fixA.getBody().getUserData() instanceof Player && fixB.getBody().getUserData() instanceof Bullet) || 
						(fixA.getBody().getUserData() instanceof Bullet && fixB.getBody().getUserData() instanceof Player)){
					return;
				}
				
				if( (fixA.getBody().getUserData() instanceof Enemy && fixB.getBody().getUserData() instanceof EnemyProjectile) || 
						(fixA.getBody().getUserData() instanceof EnemyProjectile && fixB.getBody().getUserData() instanceof Enemy)){
					return;
				}
				
				if(fixA.getBody().getUserData() instanceof Mech && fixB.getBody().getUserData() instanceof Projectile){
					Mech mech = (Mech)fixA.getBody().getUserData();
					if(mech.isInGhostState()){
						return;
					}
				}
				
				if(fixB.getBody().getUserData() instanceof Mech && fixA.getBody().getUserData() instanceof Projectile){
					Mech mech = (Mech)fixB.getBody().getUserData();
					if(mech.isInGhostState()){
						return;
					}
				}
				
				if(fixA.getBody().getUserData() instanceof Projectile){
					Projectile projectile = (Projectile)fixA.getBody().getUserData();
					if(!projectile.isFired()){
						return;
					}
				}
				
				if(fixB.getBody().getUserData() instanceof Projectile){
					Projectile projectile = (Projectile)fixB.getBody().getUserData();
					if(!projectile.isFired()){
						return;
					}
				}
				
				if(fixA.getBody().getUserData() instanceof Mech && fixB.getBody().getUserData() instanceof Mech){
					Mech m1 = (Mech) fixA.getBody().getUserData();
					Mech m2 = (Mech) fixB.getBody().getUserData();
					if(m1.isInSpikeState() && m2 instanceof Player){
						m2.dealDamage(m1.getSpikeDamage());
					}
					if(m2.isInSpikeState() && m1 instanceof Player){
						m1.dealDamage(m2.getSpikeDamage());
					}
				}

				if(fixA.getBody().getUserData() instanceof Player){
					Player player = (Player)(contact.getFixtureA().getBody().getUserData());
					if(player.isBlinking() && fixB.getBody().getUserData() instanceof Wall){
						player.stopBlinking();
					}
				}else if(fixB.getBody().getUserData() instanceof Player){
					Player player = (Player)(contact.getFixtureB().getBody().getUserData());
					if(player.isBlinking() && fixA.getBody().getUserData() instanceof Wall){
						player.stopBlinking();
					}
				}
				
				if(fixA.getBody().getUserData() instanceof Projectile){
					Projectile projectile = (Projectile)(fixA.getBody().getUserData());
					if( !(projectile instanceof SuperBullet && fixB.getBody().getUserData() instanceof Projectile) ){
						deleteProjectile(projectile, contact.getWorldManifold().getPoints()[0].mul(PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT),
								fixB);
					}
					if(fixB.getBody().getUserData() instanceof Mech){
						Mech mech = (Mech)fixB.getBody().getUserData();
						if(!(mech.isInArmorState() || mech.isInHitState())){
							mech.dealDamage(projectile.getDamage());
						}else{
							mech.showShield(projectile.getXInScreenSpace(), projectile.getYInScreenSpace());
						}
					}
				}
				
				if(fixB.getBody().getUserData() instanceof Projectile){
					Projectile projectile = (Projectile)(fixB.getBody().getUserData());
					if( !(projectile instanceof SuperBullet && fixA.getBody().getUserData() instanceof Projectile) ){
						deleteProjectile(projectile, contact.getWorldManifold().getPoints()[0].mul(PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT),
								fixA);
					}
					if(fixA.getBody().getUserData() instanceof Mech){
						Mech mech = (Mech)fixA.getBody().getUserData();
						if(!(mech.isInArmorState() || mech.isInHitState())){
							mech.dealDamage(projectile.getDamage());
						}else{
							mech.showShield(projectile.getXInScreenSpace(), projectile.getYInScreenSpace());
						}
					}
				}
				
				if(fixA.getBody().getUserData() instanceof Mech && 
						fixB.getBody().getUserData() instanceof Wall){
					Mech mech = (Mech) fixA.getBody().getUserData();
					mech.wallClinged = true;
				}
				
				if(fixB.getBody().getUserData() instanceof Mech && 
						fixA.getBody().getUserData() instanceof Wall){
					Mech mech = (Mech) fixB.getBody().getUserData();
					mech.wallClinged = true;
				}
				
			}
			
			public void deleteProjectile(Projectile projectile, Vector2 contactPoint, Fixture whatDestroyed){
				if(!(projectile instanceof HealingProjectile || projectile instanceof BouncingBullet || projectile instanceof DestroyableBouncingBullet)){
					ParticleManager.getInstance().sparkle(contactPoint.x, contactPoint.y);
				}
				projectile.destroy(whatDestroyed);
				
			}

			@Override
			public void endContact(Contact contact) {
				
				Fixture fixA = contact.getFixtureA();
				Fixture fixB = contact.getFixtureB();
				
				if(fixA.getBody().getUserData() instanceof Mech && 
						fixB.getBody().getUserData() instanceof Wall){
					Mech mech = (Mech) fixA.getBody().getUserData();
					mech.wallClinged = false;
				}
				
				if(fixB.getBody().getUserData() instanceof Mech && 
						fixA.getBody().getUserData() instanceof Wall){
					Mech mech = (Mech) fixB.getBody().getUserData();
					mech.wallClinged = false;
				}
				
			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				
				Fixture fixA = contact.getFixtureA();
				Fixture fixB = contact.getFixtureB();
				
				if(fixA.getBody().getUserData() instanceof Mech){
					Mech mech = (Mech)fixA.getBody().getUserData();
					if(mech.isInGhostState()){
						if(!(fixB.getBody().getUserData() instanceof Wall)){
							contact.setEnabled(false);
						}
					}
				}
				
				if(fixB.getBody().getUserData() instanceof Mech){
					Mech mech = (Mech)fixB.getBody().getUserData();
					if(mech.isInGhostState()){
						if(!(fixA.getBody().getUserData() instanceof Wall)){
							contact.setEnabled(false);
						}
					}
				}
				
				if( (fixA.getBody().getUserData() instanceof Player && fixB.getBody().getUserData() instanceof Bullet) || 
						(fixA.getBody().getUserData() instanceof Bullet && fixB.getBody().getUserData() instanceof Player)){
					contact.setEnabled(false);
				}
				if( (fixA.getBody().getUserData() instanceof Enemy && fixB.getBody().getUserData() instanceof EnemyProjectile) || 
						(fixA.getBody().getUserData() instanceof EnemyProjectile && fixB.getBody().getUserData() instanceof Enemy)){
					contact.setEnabled(false);
				}
				
				if(fixA.getBody().getUserData() instanceof Projectile){
					Projectile projectile = (Projectile)fixA.getBody().getUserData();
					if(!projectile.isFired()){
						contact.setEnabled(false);;
					}
				}
				if(fixB.getBody().getUserData() instanceof Projectile){
					Projectile projectile = (Projectile)fixB.getBody().getUserData();
					if(!projectile.isFired()){
						contact.setEnabled(false);
					}
				}
				
				if(fixA.getBody().getUserData() instanceof Projectile && fixB.getBody().getUserData() instanceof Projectile){
					if(fixA.getBody().getUserData().getClass().equals(fixB.getBody().getUserData().getClass())){
						contact.setEnabled(false);
					}
				}
				
				if(fixA.getBody().getUserData() instanceof EnemyProjectile && fixB.getBody().getUserData() instanceof EnemyProjectile){
					contact.setEnabled(false);
				}
				
				if(fixA.getBody().getUserData() instanceof Enemy && fixB.getBody().getUserData() instanceof Enemy){
					contact.setEnabled(false);
				}
				
				if(fixA.getBody().getUserData() instanceof Drone &&
						fixB.getBody().getUserData() instanceof HealingProjectile){
					contact.setEnabled(false);
				}
				
				if(fixB.getBody().getUserData() instanceof Drone &&
						fixA.getBody().getUserData() instanceof HealingProjectile){
					contact.setEnabled(false);
				}
				
				if((fixA.getBody().getUserData() instanceof HealingProjectile &&
						fixB.getBody().getUserData() instanceof EnemyProjectile) ||
						(fixB.getBody().getUserData() instanceof HealingProjectile &&
								fixA.getBody().getUserData() instanceof EnemyProjectile)){
					contact.setEnabled(false);
				}
				
				if((fixA.getBody().getUserData() instanceof Bullet && fixB.getBody().getUserData() instanceof FireProjectile) ||
						(fixB.getBody().getUserData() instanceof Bullet && fixA.getBody().getUserData() instanceof FireProjectile)){
					contact.setEnabled(false);
				}
				
				if(fixA.getBody().getUserData() instanceof PhantomProjectile ||
						fixB.getBody().getUserData() instanceof PhantomProjectile){
					contact.setEnabled(false);
				}
				
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				
				
				
			}
		};
		return contactListener;
			
	}
	
	public void onBackKeyPressed(){
		hud.setControlsVisible(false);
		hud.setMenuVisible(true);
		hud.setSkipButtonVisible(false);
	}
	
	

}
