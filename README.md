This repository contains the MapReduce programs that I created as part of a research project studying the impact of Covid-19 on crime rates in New York City. The intent of this analytic is to provide insight to the reporting of crimes and how the public may perceive crime rates based on social media news outlets.

MapReduce Overview:

These MapReduce files remove any records that have fewer than the required number of fields. It outputs the two columns needed for my further analysis: date and tweet. The cleaning MapReduce program takes the original data file tweets.csv.

profiling_code: The MapReduce code in this directory outputs the records that are viable for use in further analysis. This program is run on the output data from the data cleaning program.

ana_code: The zip files in this directory contain the three main jobs necessary for data analysis. The Crime_Job file represents the MapReduce job that filters out tweets that do not mention keywords related to crimes. The Date_Job file contains the MapReduce job that maps each date to its sum of crime-related tweets. Finally, the Week_Job file is the MapReduce job that calculates weekly sums throughout our pre-determined dates (1 incomplete week January 1st through 4th and 51 consecutive weeks beginning the first Sunday of 2020). The WeekMapper program can be edited for a different period by changing the Date variable beg to the initial date, end to 6 days after beg, and the upper boundary of the for loop can be edited based on the desired number of weeks. The goal of the output of the analytic code is to allow me to identify trends in crimes that have value to the public according to @nypost's Twitter. This can allow for visualization along with Covid-19 case trends and New York City public offense data as well as comparison to important Covid-related dates to determine any correlations. 

Compilation and Job Run Walkthrough:

Begin by using scp to put all the Java files and the csv into the Peel cluster.

Use "hdfs dfs -put tweets.csv" to move the data file into HDFS.

To compile the Mapper and Reducer programs, run the command "javac -classpath 'yarn classpath' -d . ClassName.Java", and to compile the Driver class, use the command "javac -classpath 'yarn classpath':. -d . DriverName.java".

When they have all been compiled, create a jar file containing the compiled class files using "jar -cvf jarName.jar MapperName.class ReducerName.class DriverName.class".

To run the MapReduce program on the data, use the command "hadoop jar jarName.jar DriverName inputfile.csv outputdirectory".

Finally, to view the results of each job, use the command "hdfs dfs -cat outputdirectory/part-r-00000".

Flow of Files:

input: CleanMapper.java CleanReducer.java Clean.java, inputfile = tweets.csv, output directory = cleandata
output: cleandata/part-r-00000, found in HDFS

(optional for running other jobs, used to profile data) input: CountRecsMapper.java CountRecsReducer.java CountRecs.java, inputfile = cleandata/part-r-00000, output directory = rectotal
output: rectotal/part-r-00000, found in HDFS

input: CrimeMapper.java CrimeReducer.java CrimeTweets.java, inputfile = cleandata/part-r-00000, output directory = crimesbyday
output: crimesbyday/part-r-00000, found in HDFS

input: CrimeMapper.java CrimeReducer.java CrimeTweets.java, inputfile = cleandata/part-r-00000, output directory = crimesbyday
output: crimesbyday/part-r-00000, found in HDFS

input: DateMapper.java DateReducer.java Dates.java, inputfile = crimesbyday/part-r-00000, output directory = tweetsbydate
output: tweetsbydate/part-r-00000, found in HDFS

input: WeekMapper.java WeekReducer.java WeekSums.java, inputfile = tweetsbydate/part-r-00000, output directory = sumsbyweek
output: sumsbyweek/part-r-00000, found in HDFS


