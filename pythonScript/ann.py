from multiprocessing.dummy import Array
import numpy as np
import matplotlib.pyplot as plt
import csv
import pandas
# source ~/.my-env/bin/activate
#         return cumulative_errors

# There’s a lot going on in the above code block, so here’s a line-by-line breakdown:

#     Line 8 picks a random instance from the dataset.

#     Lines 14 to 16 calculate the partial derivatives and return the derivatives for the bias and the weights. They use _compute_gradients(), which you defined earlier.

# Line 18 updates the bias and the weights using _update_parameters(), which you defined in the previous code block.

# Line 21 checks if the current iteration index is a multiple of 100. You do this to observe how the error changes every 100 iterations.

# Line 24 starts the loop that goes through all the data instances.

# Line 28 computes the prediction result.

# Line 29 computes the error for every instance.

# Line 31 is where you accumulate the sum of the errors using the cumulative_error variable. You do this because you want to plot a point with the error for all the data instances. Then, on line 32, you append the error to cumulative_errors, the array that stores the errors. You’ll use this array to plot the graph.


class NeuralNetwork:
    def __init__(self, learning_rate):
        self.supComWeight = 0.6
        self.weights1 = np.array([self.supComWeight, self.supComWeight])
        self.weights2 = np.array([self.supComWeight, self.supComWeight])
        self.weights3 = np.array([self.supComWeight, self.supComWeight])
        self.bias = 0.0
        self.learning_rate = learning_rate

    def _sigmoid(self, x):
        return 1 / (1 + np.exp(-x))

    def _sigmoid_deriv(self, x):
        return self._sigmoid(x) * (1 - self._sigmoid(x))

    def predict(self, input_vector):
        neuron1 = np.dot(input_vector, self.weights1) + self.bias
        neuron2 = np.dot(input_vector, self.weights2) + self.bias
        nOutp1 = self._sigmoid(neuron1)
        nOutp2 = self._sigmoid(neuron2)
        input_vector2 = np.array([nOutp1,nOutp2])
        neuronOutput = np.dot(input_vector2,self.weights3)
        layer_2 = self._sigmoid(neuronOutput)
        prediction = layer_2
        return prediction

    def _compute_gradients(self, input_vector, target):
        neuron1 = np.dot(input_vector, self.weights1) + self.bias
        neuron2 = np.dot(input_vector, self.weights2) + self.bias
        nOutp1 = self._sigmoid(neuron1)
        nOutp2 = self._sigmoid(neuron2)
        input_vector2 = np.array([nOutp1,nOutp2])
        neuronOutput = np.dot(input_vector2,self.weights3)
        layer_2 = self._sigmoid(neuronOutput)
        prediction = layer_2

        derror_dprediction = 2 * (prediction - target)
        dprediction_dlayer2 = self._sigmoid_deriv(neuronOutput)
        # dlayer2_dbias = 1
        dlayer2_dweights3 = (0 * self.weights3) + (1 * input_vector2)

        dnOutp1_ddlayer1 = self._sigmoid_deriv(neuron1)
        dlayer1_dweights1 = (0 * self.weights1) + (1 * input_vector)

        dnOutp2_ddlayer1 = self._sigmoid_deriv(neuron2)
        dlayer1_dweights2 = (0 * self.weights2) + (1 * input_vector)
        derror_dbias = 0
        # derror_dbias = (
        #     derror_dprediction * dprediction_dlayer1 * dlayer1_dbias
        # )
        derror_dweights3 = (
            derror_dprediction * dprediction_dlayer2 * dlayer2_dweights3
        )
        dnOutp2_dweights2 = (
            dnOutp2_ddlayer1 * dlayer1_dweights2
        )
        dnOutp1_dweights1 = (
            dnOutp1_ddlayer1 * dlayer1_dweights1
        )
        return derror_dbias, derror_dweights3, dnOutp2_dweights2, dnOutp1_dweights1

    def _update_parameters(self, derror_dbias, derror_dweights3, dnOutp2_dweights2, dnOutp1_dweights1):
        self.bias = self.bias - (derror_dbias * self.learning_rate)
        self.weights3 = self.weights3 - (derror_dweights3 * self.learning_rate)
        self.weights2 = self.weights2 - (dnOutp2_dweights2 * self.learning_rate)
        self.weights1 = self.weights1 - (dnOutp1_dweights1 * self.learning_rate)

    def train(self, input_vectors, targets, iterations):

        cumulative_errors = []

        for current_iteration in range(iterations):

            # Pick a data instance at random

            random_data_index = np.random.randint(len(input_vectors))

            input_vector = input_vectors[random_data_index]

            target = targets[random_data_index]

            # Compute the gradients and update the weights

            derror_dbias, derror_dweights3, dnOutp2_dweights2, dnOutp1_dweights1 = self._compute_gradients(

                input_vector, target

            )

            self._update_parameters(derror_dbias, derror_dweights3, dnOutp2_dweights2, dnOutp1_dweights1)

            # Measure the cumulative error for all the instances

            if current_iteration % 100 == 0:

                cumulative_error = 0

                # Loop through all the instances to measure the error

                for data_instance_index in range(len(input_vectors)):

                    data_point = input_vectors[data_instance_index]

                    target = targets[data_instance_index]

                    prediction = self.predict(data_point)

                    error = np.square(prediction - target)

                    cumulative_error = cumulative_error + error

                cumulative_errors.append(cumulative_error)

        return cumulative_errors


def main():
    print("marke a prediction!")
    learning_rate = 0.1
    input_vectors = np.array(
        [[3, 1.5], [2, 1],
            [4, 1.5],
            [3, 4],
            [3.5, 0.5],
            [2, 0.5],
            [5.5, 1],
            [1, 1],
         ]
    )
    targets = np.array([0, 1, 0, 1, 0, 1, 1, 0])
    input_vector = input_vectors[0]
    neural_network = NeuralNetwork(learning_rate)
    input_vector = [0.6,0.75]
    prediction = neural_network.predict(input_vector)
    print(f"The prediction result is: {prediction}")
    training_error = neural_network.train(input_vectors, targets, 10000)
    plt.plot(training_error)
    plt.xlabel("Iterations")
    plt.ylabel("Error for all training instances")
    plt.savefig("cumulative_error.png")

    # traitment 1
    file = open('outputmax.txt') #outputmax.txt output.txt
    # Read & print the entire file
    # print(file.read())
    # Read and print the entire file line by line
    nbT = 4
    line = file.readline()
    cpt = 0  # indice numero ligne ds le fichier
    # datasets = np.array([],[])
    i = 0
    prec = 0.1
    while line != '':  # The EOF char is an empty string
        print(line)
        tab = line.split(' ')
        t1 = (tab[len(tab)-1]).split(';')
        arr = [0,0,'']
        arr[0] = float(t1[0])
        arr[1] = float(t1[1])
        # TODO GET PATTERN 
        arr[2] = (line.split('#SUP')[0])
        with open('datasets.csv', mode='a') as dataset_file:
            dataset_writer = csv.writer(dataset_file, delimiter=',', quotechar='"', quoting=csv.QUOTE_MINIMAL)
            dataset_writer.writerow(arr)
        print(t1)
        print(tab[len(tab)-1])
        # e = int(tab[len(tab) - 1])
        
        # print(cpt, ' ', e/nbT)
        line = file.readline()
        cpt = cpt+1
    # print('datasets ', datasets)
    df = pandas.read_csv('datasets.csv',on_bad_lines='skip', sep=',', header=None)
    print(df)
    input_vector = [];
    ouput_patterns = [];
    k= 0
    # Iterate all rows using DataFrame.iterrows()
    for i in range(len(df)) :
        print(df.iloc[i, 0],type(df.iloc[i, 0]), df.iloc[i, 1],type(df.iloc[i, 0]))
        input_vector = [df.iloc[i, 0],df.iloc[i, 1]]
        # input_vectors[1] = df.iloc[i, 0];
        neural_network = NeuralNetwork(learning_rate)
        prediction = neural_network.predict(input_vector)
        print(f"The prediction result is: {prediction}")
        if prediction >= prec :
            ouput_patterns = np.append(ouput_patterns, df.iloc[i, 2]+'-2'+' #SUP: '+str(round(df.iloc[i, 0],2))+';'+str(round(df.iloc[i, 1],2))+';'+str(round(prediction, 2)))
            # ouput_patterns[k]=i
            print (' pos : ', i)
    print (ouput_patterns)
    out_file = open("output2.txt", "w+")
    for row in ouput_patterns:
        out_file.write(row)
        out_file.write('\n')
        # np.save(out_file, str(row))
    out_file.close()

if __name__ == "__main__":
    main()
