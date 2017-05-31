import scala.language.postfixOps
import squants.energy.{Energy, Joules, Milliwatts, Power, WattHours, Watts}
import squants.time.{Seconds, Time}

case class Light(isOn: Boolean, power: Power )

case class Lights(theLights: List[Light], number: Int, roundTime: Time ) {

  override def toString: String = theLights.map(l => if (l.isOn) "O" else "-").foldLeft("")(_ + _)

  def sumOnCycles(startRound: Int, endRound: Int, lightPos: Int): Int = {
    def delta(lp: Int, r: Int) = if (lp % r == 0) 1 else 0
    def loop(round: Int, acc: Int): Int = {
      if (round == endRound) acc + delta(lightPos, round)
      else loop(round+1, acc+delta(lightPos,round))
    }
    require(endRound >= startRound)
    loop(startRound, 0)
  }

  def totalOnCycles(startRound: Int, endRound: Int): Int = {
    (1 to number)
      .map(pos => sumOnCycles(startRound, endRound, pos)).sum
  }

  def totalEnergy(startRound: Int, endRound: Int): Energy= {
    (0 until number)
    .map(i => sumOnCycles(startRound, endRound, i) * theLights(i).power * roundTime)
      .foldLeft(WattHours(0))((i:Energy, j:Energy) => i+j)
  }
}
object Lights{
  def apply (number: Int): Lights = {
    new Lights (List.fill(number)(Light(isOn = true, Milliwatts(5) )), number, Seconds(1))
  }
  def apply(lights: List[Light]) = new Lights(lights, lights.length, Seconds(1))
}

  val myLights = Lights(7)
  def turnOnNth(n: Int, lights: Lights) = {
    val lightList = (0 until lights.number)
      .map(i => if (i % n == 0) Light(isOn = true, Milliwatts(5)) else Light(isOn = false, Milliwatts(5))).toList
    Lights(lightList)
  }

turnOnNth(1,myLights)

myLights.sumOnCycles(startRound = 1,endRound =3,lightPos=1)

val onCycles = myLights.totalOnCycles(startRound = 1,endRound = 2)

val energy: Energy =  myLights.totalEnergy(startRound = 1,endRound = 100000000)

val joules = energy in Joules












