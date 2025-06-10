#include <iostream>

using namespace std;

int main() {
    ios::sync_with_stdio(false);
    cin.tie(NULL);
    cout.tie(NULL);

    double A, B;

    cin >> A >> B;

    cout<<fixed;
    cout.precision(18);

    cout << A / B;
}