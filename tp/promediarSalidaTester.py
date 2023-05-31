cantidad_de_iteraciones = 100;

def main():
	salidaTester = open('salidaTester.txt', 'r')
	lineasSalida = salidaTester.readlines()

	dictPromedios = {"locksFinos": [0]*7, "optimista": [0]*7, "sinLocks": [0]*7}

	mapLocksFinos = [2, 4, 6, 8, 10, 12, 14]
	mapOptimista = [16, 18, 20, 22, 24, 26, 28]
	mapSinLocks = [30, 32, 34, 36, 38, 40, 42]

	i = 0
	for linea in lineasSalida:
		if i == 2 or i == 4 or i == 6 or i == 8 or i == 10 or i == 12 or i == 14:
			result = float(linea.split(" ")[4])
			currentExperiments = dictPromedios["locksFinos"]
			currentExperiments[mapLocksFinos.index(i)] = currentExperiments[mapLocksFinos.index(i)] + result
			dictPromedios["locksFinos"] = currentExperiments
		elif i == 16 or i == 18 or i == 20 or i == 22 or i == 24 or i == 26 or i == 28:
			result = float(linea.split(" ")[4])
			currentExperiments = dictPromedios["optimista"]
			currentExperiments[mapOptimista.index(i)] = currentExperiments[mapOptimista.index(i)] + result
			dictPromedios["optimista"] = currentExperiments
		elif i == 30 or i == 32 or i == 34 or i == 36 or i == 38 or i == 40 or i == 42:
			result = float(linea.split(" ")[4])
			currentExperiments = dictPromedios["sinLocks"]
			currentExperiments[mapSinLocks.index(i)] = currentExperiments[mapSinLocks.index(i)] + result
			dictPromedios["sinLocks"] = currentExperiments
		elif i == 43:
			i = 1
			continue
		i += 1


	for key in dictPromedios:
		resultadosSinDividir = dictPromedios[key]
		resultadosPromediados = [x / cantidad_de_iteraciones for x in resultadosSinDividir]

		dictPromedios[key] = resultadosPromediados

	print(dictPromedios)


main()