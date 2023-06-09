import json
import matplotlib.pyplot as plt

variacionMaximaDeHilos = 10;

def plotHilosManteniendoOperacionesTotales():
	file = open("variandoHilosTotalesManteniendoOperacionesTotales.json")
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


def plotHilosManteniendoOperacionesPorHilo():
	file = open("variandoHilosTotalesManteniendoOperacionesPorHilo.json")
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

def plotAgregarVsQuitar():
	file = open("agregarVsQuitar.json")
	dict = json.load(file)

	keys = [str(x*10) + "%" for x in range(0, 11)]
	locksFinos = []
	optimista = []
	sinLocks = []
	for key in keys:
		list = dict[key]
		locksFinos.append(float(list[0].split(": ")[1]))
		optimista.append(float(list[1].split(": ")[1]))
		sinLocks.append(float(list[2].split(": ")[1]))

	x = [x for x in range(0,101, 10)]
	lFino = plt.scatter(x, locksFinos, color='b')
	lOptimista = plt.scatter(x, optimista, color='c')
	lSinLocks = plt.scatter(x, sinLocks, color='y')

	plt.legend((lFino, lOptimista, lSinLocks), ("Locks Finos", "Optimista", "Sin Locks"))
	plt.xlabel("Proporción de hilos que agregan (%)")
	plt.ylabel("Tiempo (ms)")
	plt.title("Performance variando proporción de hilos que agregan vs hilos que quitan")
	plt.show()
	file.close()

def plotAgregarVsPertenecer():
	file = open("agregarVsPertenecer.json")
	dict = json.load(file)

	keys = [str(x*10) + "%" for x in range(0, 11)]
	locksFinos = []
	optimista = []
	sinLocks = []
	for key in keys:
		list = dict[key]
		locksFinos.append(float(list[0].split(": ")[1]))
		optimista.append(float(list[1].split(": ")[1]))
		sinLocks.append(float(list[2].split(": ")[1]))

	x = [x for x in range(0,101, 10)]
	lFino = plt.scatter(x, locksFinos, color='b')
	lOptimista = plt.scatter(x, optimista, color='c')
	lSinLocks = plt.scatter(x, sinLocks, color='y')

	plt.legend((lFino, lOptimista, lSinLocks), ("Locks Finos", "Optimista", "Sin Locks"))
	plt.xlabel("Proporción de hilos que agregan (%)")
	plt.ylabel("Tiempo (ms)")
	plt.title("Performance variando proporción de hilos que agregan vs hilos que hacen pertenecer")
	plt.show()
	file.close()

def plotQuitarVsPertenecer():
	file = open("quitarVsPertenecer.json")
	dict = json.load(file)

	keys = [str(x*10) + "%" for x in range(0, 11)]
	locksFinos = []
	optimista = []
	sinLocks = []
	for key in keys:
		list = dict[key]
		locksFinos.append(float(list[0].split(": ")[1]))
		optimista.append(float(list[1].split(": ")[1]))
		sinLocks.append(float(list[2].split(": ")[1]))

	x = [x for x in range(0,101, 10)]
	lFino = plt.scatter(x, locksFinos, color='b')
	lOptimista = plt.scatter(x, optimista, color='c')
	lSinLocks = plt.scatter(x, sinLocks, color='y')

	plt.legend((lFino, lOptimista, lSinLocks), ("Locks Finos", "Optimista", "Sin Locks"))
	plt.xlabel("Proporción de hilos que quitan (%)")
	plt.ylabel("Tiempo (ms)")
	plt.title("Performance variando proporción de hilos que quitan vs hilos que hacen pertenecer")
	plt.show()
	file.close()

def plot():
	plotAgregarVsQuitar()
	plotAgregarVsPertenecer()
	plotQuitarVsPertenecer()
	plotHilosManteniendoOperacionesTotales()
	plotHilosManteniendoOperacionesPorHilo()


plot()