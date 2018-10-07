# Multinomial Logistic Regression Demo

Data comes from UCI's collection of [Activities of Daily Living](https://archive.ics.uci.edu/ml/datasets/Activities+of+Daily+Living+%28ADLs%29+Recognition+Using+Binary+Sensors) data set.

## Output:

```
INFO: Creating tables.
INFO: Inserted 409 sensor readings.
INFO: Inserted 248 labels.
INFO: ================================================================================
INFO: ================================================================================
INFO: Multinomial Regression Average Results (over 100 runs):
INFO: Training Data Accuracy:
INFO:     Average: 80.23%
INFO:     Min: 73.74%
INFO:     Max: 88.89%
INFO: Test Data Accuracy:
INFO:     Average: 92.62%
INFO:     Min: 80.0%
INFO:     Max: 100.0%
INFO: Where we went wrong on the worst classifier's test data:
INFO:     Expected: Toileting
INFO:         Features: [1.0, -0.9117620435770886, -0.9099612708765433, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0, 2.0, 0.0, 0.0, 0.0]
INFO:         Top 3: Grooming(0.562) Toileting(0.1813) Showering(0.0106) Snack(0.0074) Lunch(0.004) 
INFO:     Expected: Toileting
INFO:         Features: [1.0, -0.9743700647852351, -0.9753423205085126, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, 1.0, 0.0, 0.0, 2.0, 0.0, 1.0, 0.0]
INFO:         Top 3: Spare_Time(1.0) Toileting(0.0) Sleeping(0.0) Snack(0.0) Breakfast(0.0) 
INFO:     Expected: Toileting
INFO:         Features: [1.0, -0.4186597375374281, -0.4146932426562401, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0, 2.0, 0.0, 0.0, 0.0]
INFO:         Top 3: Grooming(0.7809) Toileting(0.3147) Showering(0.0357) Snack(0.0168) Lunch(0.0137) 
INFO:     Expected: Toileting
INFO:         Features: [1.0, -0.688354575693754, -0.688354575693754, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0]
INFO:         Top 3: Grooming(0.5109) Toileting(0.0312) Lunch(0.0135) Snack(0.0108) Showering(0.0102) 
INFO:     Expected: Breakfast
INFO:         Features: [1.0, 0.20791169081775931, 0.17364817766693028, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 3.0, 2.0, 0.0, 2.0, 0.0, 0.0, 5.0, 0.0, 0.0, 7.0, 0.0, 0.0]
INFO:         Top 3: Snack(0.0511) Sleeping(0.0313) Lunch(0.0182) Breakfast(0.0048) Leaving(8.0E-4) 
INFO:     Expected: Breakfast
INFO:         Features: [1.0, -0.2419218955996675, -0.25460194820552745, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 2.0, 1.0, 0.0, 2.0, 0.0, 0.0, 3.0, 0.0, 0.0, 5.0, 0.0, 0.0]
INFO:         Top 3: Snack(0.0736) Lunch(0.024) Sleeping(0.0066) Breakfast(0.0026) Leaving(0.0025) 
INFO:     Expected: Breakfast
INFO:         Features: [1.0, -0.06104853953485694, -0.10452846326765305, 0.0, 0.0, 0.0, 0.0, 2.0, 0.0, 1.0, 0.0, 0.0, 0.0, 2.0, 1.0, 0.0, 3.0, 0.0, 0.0, 3.0, 0.0, 0.0, 6.0, 0.0, 0.0]
INFO:         Top 3: Snack(0.1374) Lunch(0.0306) Sleeping(0.0021) Leaving(0.0017) Breakfast(0.001) 
INFO:     Expected: Grooming
INFO:         Features: [1.0, -0.3786486173524327, -0.3786486173524327, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, 0.0]
INFO:         Top 3: Spare_Time(1.0) Toileting(0.0) Sleeping(0.0) Snack(0.0) Breakfast(0.0) 
INFO:     Expected: Toileting
INFO:         Features: [1.0, 0.41865973753742797, 0.41071885261347724, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 0.0, 0.0, 3.0, 0.0, 0.0, 0.0]
INFO:         Top 3: Grooming(0.977) Toileting(0.427) Showering(0.1584) Leaving(0.0631) Snack(0.0606) 
INFO:     Expected: Breakfast
INFO:         Features: [1.0, -0.04361938736533627, -0.0828082075122044, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 2.0, 3.0, 0.0, 2.0, 0.0, 0.0, 5.0, 0.0, 0.0, 7.0, 0.0, 0.0]
INFO:         Top 3: Snack(0.0326) Sleeping(0.012) Lunch(0.0046) Breakfast(0.0044) Leaving(9.0E-4) 
INFO: ================================================================================
INFO: ================================================================================
```

