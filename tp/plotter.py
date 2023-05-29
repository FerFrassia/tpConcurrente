import json
import matplotlib.pyplot as plt

variacionMaximaDeHilos = 10;

def plot():
	file = open("variandoCantidadDeHilosTotales.json")
	dict = json.load(file)

	keys = [str(x) for x in range(1, variacionMaximaDeHilos+1)]
	locksFinos = []
	optimista = []
	sinLocks = []
	for key in keys:
		list = dict[key]
		locksFinos.append(float(list[0].split(": ")[1]))
		optimista.append(float(list[1].split(": ")[1]))
		sinLocks.append(float(list[2].split(": ")[1]))

	x = [int(x) for x in keys]
	lFino = plt.scatter(x, locksFinos, color='b')
	lOptimista = plt.scatter(x, optimista, color='c')
	lSinLocks = plt.scatter(x, sinLocks, color='y')

	plt.legend((lFino, lOptimista, lSinLocks), ("Locks Finos", "Optimista", "Sin Locks"))
	plt.xlabel("Cantidad de Hilos")
	plt.ylabel("Tiempo (ms)")
	plt.title("Performance variando cantidad total de hilos")
	plt.show()
	file.close()

plot()